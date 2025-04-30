
package com.moove.controller;

import com.moove.model.UsersModel;
import com.moove.service.RegisterService;
import com.moove.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/Register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private RegisterService registerService = new RegisterService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Register form submitted - processing registration");
            
            // Extract user info from form into UsersModel using the helper method which helps to create objects of user details.
            UsersModel user = extractUserModel(req);
            
            // Call the service to register the user
            boolean isRegistered = registerService.registerUser(user);
            
            if (isRegistered) {
                // Set success message and redirect to login
                HttpSession session = req.getSession();
                session.setAttribute("successMessage", "Registration successful! Please login.");
                resp.sendRedirect("Login");
            } else {
                System.out.println("Registration failed for user: " + user.getUser_Name());
                // Be more specific about the failure reason
                String errorMsg = "Registration failed. ";
                if (registerService.isUsernameExists(user.getUser_Name())) {
                    errorMsg += "Username already exists. ";
                }
                if (registerService.isEmailExists(user.getUser_Email())) {
                    errorMsg += "Email already exists. ";
                }
                
                if (errorMsg.equals("Registration failed. ")) {
                    errorMsg += "Please check the logs for more details.";
                }
                
                req.setAttribute("error", errorMsg);
                req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            System.err.println("Exception in registration process: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
        }
    }
    
    // Helper method to extract input values from the form and build UsersModel object.
    private UsersModel extractUserModel(HttpServletRequest req) {
        UsersModel user = new UsersModel();
        
        // Get form data
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        String gender = req.getParameter("gender");
        String role = req.getParameter("role");

        // Set required user details
        user.setUser_Name(username);
        
        // Encrypt password using PasswordUtil
        String encryptedPassword = PasswordUtil.encrypt(username, password);
        if (encryptedPassword != null) {
            user.setPassword(encryptedPassword);
            System.out.println("Password encrypted successfully");
        } else {
            user.setPassword(password);
        }
        
        // Set default values for other required fields
        user.setUser_Email(email);
        user.setUser_Status("active");
        user.setUser_Address(address);
        user.setUser_Gender(gender);
        
        // Look up the role ID based on the role name selected in the form
        int roleId = registerService.getRoleIdByName(role);
        user.setRole_Id(roleId);
        System.out.println("Setting role ID: " + roleId + " for role name: " + role);
        
        System.out.println("User model created with defaults - Status: " + user.getUser_Status());
        
        return user;
    }
}