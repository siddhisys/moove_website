package com.moove.service;

import java.sql.Connection;

/*
 * Author: Siddhi Jain
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moove.config.DbConfig;
import com.moove.model.UsersModel;

public class RegisterService {
    private Connection dbConn;

    /**
     * Creating a constructor to check for database connection each time when the name is called
     */
    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Checks if a username already exists in the database
     */
    public boolean isUsernameExists(String username) {
        if (dbConn == null) return false;
        
        String sql = "SELECT COUNT(*) FROM `users` WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Checks if an email already exists in the database
     */
    public boolean isEmailExists(String email) {
        if (dbConn == null) return false;
        
        String sql = "SELECT COUNT(*) FROM `users` WHERE user_email = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get role ID by role name
     */
    public int getRoleIdByName(String roleName) {
        if (dbConn == null) return 1; // Default fallback
        
        String sql = "SELECT role_id FROM roles WHERE role_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking up role ID: " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * Register a new user in the database
     */
    public boolean registerUser(UsersModel user) {
        System.out.println("Attempting to register user.");
        
        if (dbConn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        
        // First check if username or email already exists
        if (isUsernameExists(user.getUser_Name())) {
            System.err.println("Username already exists: " + user.getUser_Name());
            return false;
        }
        
        if (isEmailExists(user.getUser_Email())) {
            System.err.println("Email already exists: " + user.getUser_Email());
            return false;
        }

        String sql = "INSERT INTO `users` (user_name, user_email, user_gender, password, user_address, user_status, role_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUser_Name());
            stmt.setString(2, user.getUser_Email());
            stmt.setString(3, user.getUser_Gender());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getUser_Address());
            stmt.setString(6, user.getUser_Status());
            stmt.setInt(7, user.getRole_Id());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Registration successful! Rows affected: " + rowsAffected);
                // Get the auto-generated user ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUser_ID(generatedKeys.getInt(1));
                        System.out.println("Generated user ID: " + user.getUser_ID());
                    }
                }
                return true;
            }
            System.out.println("No rows affected by INSERT");
            return false;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
}