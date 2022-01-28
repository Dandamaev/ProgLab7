package database;

import models.City;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseController {

    private final CollectionDBManager collectionDBManager;
    private final UserDBManager userDBManager;

    public DatabaseController(CollectionDBManager collectionDBManager, UserDBManager userDBManager) {
        this.collectionDBManager = collectionDBManager;
        this.userDBManager = userDBManager;
    }

    public ArrayList<City> getCollectionFromDB() throws SQLException {
        ArrayList<City> collection = collectionDBManager.getCollection();
        if (collection == null)
            throw new SQLException("It was not possible to fetch the collection from database");
        return collection;
    }

    public Object login(Credentials credentials) {
        try {
            int id = userDBManager.checkUserAndGetID(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return "User/Password given not found or incorrect";
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public Object register(Credentials credentials) {
        try {
            int id = userDBManager.registerUser(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return credentials;
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }



    public String addCity(City city, Credentials credentials) {
        try {
            return collectionDBManager.add(city, credentials);
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    public String updateCity(int id, City city, Credentials credentials) {
        try {
            return collectionDBManager.update(id, city, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String clearCity(Credentials credentials) {
        try {
            return collectionDBManager.deleteAll(credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String removeCity(int id, Credentials credentials) {
        try {
            return collectionDBManager.delete(id, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
