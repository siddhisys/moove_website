package com.moove.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.moove.config.DbConfig;
import com.moove.model.UsersModel;
import com.moove.util.PasswordUtil;

/**
 * Service class for handling login operations for the Moove application.
 * Connects to the database, verifies user credentials, and returns login status.
 */
public class LoginService {
    private Connection dbConn;
    private boolean isConnectionError = false;
    
    /**
     * Constructor initializes the database connection.
     * Sets the connection error flag if the connection fails.
     */
    public LoginService() {
        try {
            dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }
    
    /**
     * Authenticates user credentials against the database records.
     *
     * @param userModel the UserModel object containing user credentials
     * @return true if the user credentials are valid, false otherwise; null if a
     *         connection error occurs
     */
    public Boolean authenticateUser(UsersModel userModel) {
        if (isConnectionError) {
            System.out.println("Database connection error!");
            return null;
        }
        
        String query = "SELECT User_Name, password FROM users WHERE User_Name = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userModel.getUser_Name());
            ResultSet result = stmt.executeQuery();
            
            if (result.next()) {
                return validatePassword(result, userModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        return false;
    }
    
    /**
     * Validates the password retrieved from the database.
     *
     * @param result     the ResultSet containing the username and password from
     *                   the database
     * @param userModel  the UserModel object containing user credentials
     * @return true if the passwords match, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean validatePassword(ResultSet result, UsersModel userModel) throws SQLException {
        String dbUsername = result.getString("User_Name");
        String dbPassword = result.getString("password");
        
        return dbUsername.equals(userModel.getUser_Name())
                && PasswordUtil.decrypt(dbPassword, dbUsername).equals(userModel.getPassword());
    }
    
    /**
     * Checks if a user exists in the database.
     * 
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String User_Name) {
        if (isConnectionError) {
            return false;
        }
        
        String query = "SELECT username FROM users WHERE User_Name = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, User_Name);
            ResultSet result = stmt.executeQuery();
            
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}