package database;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static utils.AppConstant.*;

@Slf4j
public class DBConfigure {

    private Connection dbConnection = null;

    public void connect(){
        try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            log.info("Successfully connected to: " + DB_URL);
        } catch (SQLException e) {
            log.error("Unable to connect to database", e);
            System.exit(-1);
        }
    }

    public void disconnect() {
        log.info("Disconnecting the database...");
        try {
            dbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getDbConnection(){
        return this.dbConnection;
    }
}
