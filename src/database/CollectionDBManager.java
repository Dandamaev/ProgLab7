package database;

import models.City;
import models.Coordinates;
import models.Government;
import models.Human;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CollectionDBManager {

    private final Connection connection;

    public CollectionDBManager(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<City> getCollection() throws SQLException {
        ArrayList<City> collection = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.CITY);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
            Human human = new Human(rs.getInt("age"));
            City city = new City(
                    rs.getLong("id"),
                    rs.getString("name"),
                    new Coordinates(rs.getFloat("x"), rs.getDouble("y")),
                    creationDate,
                    rs.getDouble("area"),
                    rs.getLong("population"),
                    rs.getLong("meters_above_sea_level"),
                    rs.getDouble("timezone"),
                    rs.getBoolean("capital"),
                    Government.byOrdinal(rs.getInt("goverment")),
                    human
            );

            collection.add(city);
        }

        return collection;
    }

    public boolean hasPermissions(Credentials credentials, int cityID) throws SQLException {
        if (credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return true;

        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.USER_HAS_PERMISSIONS);
        int pointer = 0;
        preparedStatement.setInt(1, credentials.id);
        preparedStatement.setInt(2, cityID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("exists");
        }
        return false;
    }

    public String add(City city, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.CITY);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(city.getCreationDate().atStartOfDay()));
            preparedStatement.setDouble(3, city.getArea());
            preparedStatement.setLong(4, city.getPopulation());
            preparedStatement.setLong(5, city.getMetersAboveSeaLevel());
            preparedStatement.setDouble(6, city.getTimezone());
            preparedStatement.setBoolean(7, city.getCapital());
            preparedStatement.setInt(8, city.getGovernment().getId() + 1);
            ResultSet rs = preparedStatement.executeQuery();
            int cityID = 0;
            if (rs.next())
                cityID = rs.getInt(1);

            preparedStatement = connection.prepareStatement(SqlQuery.Add.COORDINATE);
            preparedStatement.setFloat(1, city.getCoordinates().getX());
            preparedStatement.setDouble(2, city.getCoordinates().getY());
            preparedStatement.setInt(3, cityID);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Add.HUMAN);
            preparedStatement.setInt(1, city.getGovernor().getAge());
            preparedStatement.setInt(2, cityID);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Add.CITY_USER_RELATIONSHIP);
            preparedStatement.setInt(1, credentials.id);
            preparedStatement.setInt(2, cityID);
            preparedStatement.executeUpdate();

            connection.commit();

            return String.valueOf(cityID);
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String update(int id, City city, Credentials credentials) throws SQLException {
        if (!hasPermissions(credentials, id))
            return "You don't have permissions";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Update.CITY);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(city.getCreationDate().atStartOfDay()));
            preparedStatement.setDouble(3, city.getArea());
            preparedStatement.setLong(4, city.getPopulation());
            preparedStatement.setLong(5, city.getMetersAboveSeaLevel());
            preparedStatement.setDouble(6, city.getTimezone());
            preparedStatement.setBoolean(7, city.getCapital());
            preparedStatement.setInt(8, city.getGovernment().getId() + 1);
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Update.COORDINATE);
            preparedStatement.setFloat(1, city.getCoordinates().getX());
            preparedStatement.setDouble(2, city.getCoordinates().getY());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Add.HUMAN);
            preparedStatement.setInt(1, city.getGovernor().getAge());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public int getCityByID(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.CITY_BY_ID);
        int pointer = 0;
        preparedStatement.setInt(++pointer, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public String deleteAll(Credentials credentials) throws SQLException {
        if (!credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return "You don't have permissions to delete all database, only root";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ALL_CITY);
            preparedStatement.executeUpdate();
            connection.commit();
            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String delete(int id, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            int dragonID = getCityByID(id);
            if (!hasPermissions(credentials, dragonID))
                return "You don't have permissions";

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.CITY_BY_ID);
            int pointer = 0;
            preparedStatement.setInt(++pointer, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

}
