package database;

public class SqlQuery {
    public static class Get {
        //City
        public static final String CITY = "SELECT city.id, city.name, coordinates.x, coordinates.y, city.creation_date, city.area, city.population, city.meters_above_sea_level, city.timezone, city.capital, city.goverment, city_human.age\n" +
                "FROM city\n" +
                "    INNER JOIN coordinates ON city.id = coordinates.city_id\n" +
                "    INNER JOIN city_human ON city.id = city_human.city_id";

        public static final String CITY_BY_ID = "SELECT id FROM city where id = ?";

        //Users
        public static final String USERS = "SELECT * FROM users";
        public static final String PASS_USING_USERNAME = "SELECT password, id FROM users WHERE username = ?";
        public static final String ID_USING_USERNAME = "SELECT id FROM users WHERE username = ?";

        public static final String USER_HAS_PERMISSIONS = "" +
                "SELECT exists(SELECT 1 from users_city where user_id = ? AND city_id = ?)";
    }
    public static class Add {
        //City
        public static final String CITY = "" +
                "INSERT INTO city(name, creation_date, area, population, meters_above_sea_level, timezone, capital, goverment)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        public static final String COORDINATE = "" +
                "INSERT INTO coordinates(x, y, city_id) " +
                "VALUES(?, ?, ?)";

        public static final String HUMAN = "" +
                "INSERT INTO city_human(age, city_id) " +
                "VALUES(?, ?)";

        //Users
        public static final String USER = "" +
                "INSERT INTO users(username, password) VALUES(?, ?)";

        public static final String CITY_USER_RELATIONSHIP = "" +
                "INSERT INTO users_city VALUES (?, ?)";
    }
    public static class Update {
        public static final String CITY = "" +
        "UPDATE city SET name=?, creation_date=?, area=?, population=?, meters_above_sea_level=?, timezone=?, capital=?, goverment=?\n"+
                "WHERE city.id = ?;";
        public static final String COORDINATE = "" +
                "UPDATE coordinates SET x = ?, y = ? WHERE city_id = ?";
        public static final String HUMAN = "" +
                "UPDATE city_human SET age = ? WHERE city_id = ?";
    }
    public static class Delete {
        public static final String ALL_CITY = "DELETE FROM city";
        public static final String CITY_BY_ID = "DELETE FROM city where id = ?";

        public static final String USER = "DELETE FROM users where username = ?";
    }
}
