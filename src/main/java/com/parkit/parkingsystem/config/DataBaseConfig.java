package com.parkit.parkingsystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Class containing the Database configuration
 */
public class DataBaseConfig {

    private static final Dotenv env = Dotenv.load();

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    private static final String USERNAME = env.get("DB_USERNAME");
    private static final String PASSWORD = env.get("DB_PASSWORD");

    /**
     * @return a Connection Object Instantiation to open
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("DBConnectée");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod", USERNAME, PASSWORD);
    }

    /**
     * @param con a Connection Object Instantiation to close
     */
    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    /**
     * @param ps a PreparedStatement Object Instantiation to close
     */
    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }
    /**
     * @param rs a ResultSet Object Instantiation to close
     */
    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
