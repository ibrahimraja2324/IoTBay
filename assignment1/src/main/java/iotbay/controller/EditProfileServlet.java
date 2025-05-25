package iotbay.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import iotbay.dao.DBManager;
import iotbay.dao.LogDAO;
import iotbay.dao.UserDAO;
import iotbay.model.Log;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EditProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditProfileServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String currentPassword = request.getParameter("currentPassword");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String email = currentUser.getEmail();

        // Clear any previous error messages
        session.setAttribute("editError", null);
        session.setAttribute("passwordError", null);
        session.setAttribute("dbError", null);

        // Validate inputs
        Validator validator = new Validator();
        boolean hasError = false;

        if (firstName == null || firstName.trim().isEmpty() || 
            lastName == null || lastName.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty()) {
            session.setAttribute("editError", "All fields are required.");
            hasError = true;
        }

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            session.setAttribute("editError", "Current password is required.");
            hasError = true;
        }

        // Check database connection
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            session.setAttribute("dbError", "Database connection not available. Please try again later.");
            request.getRequestDispatcher("edit.jsp").forward(request, response);
            return;
        }

        // Verify current password
        try {
            UserDAO userDAO = new UserDAO(manager.getConnection());
            User verifiedUser = userDAO.findUser(email, currentPassword);
            if (verifiedUser == null) {
                session.setAttribute("editError", "Current password is incorrect.");
                hasError = true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error verifying password", ex);
            session.setAttribute("dbError", "Database error occurred. Please try again.");
            hasError = true;
        }

        // Handle password change
        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(confirmPassword)) {
                session.setAttribute("passwordError", "New passwords do not match.");
                hasError = true;
            } else if (!validator.validatePassword(password)) {
                session.setAttribute("passwordError", "Password must be at least 4 characters.");
                hasError = true;
            }
        } else {
            password = currentUser.getPassword(); // Keep existing password
        }

        if (hasError) {
            request.getRequestDispatcher("edit.jsp").forward(request, response);
            return;
        }

        // Update user profile
        try {
            UserDAO userDAO = new UserDAO(manager.getConnection());
            User updatedUser = new User(firstName, lastName, email, password, phone, currentUser.getRole());
            updatedUser.setActive(currentUser.isActive());
            
            if (userDAO.updateUser(updatedUser)) {
                session.setAttribute("currentUser", updatedUser);
                Log log = new Log(email, "Profile edited", updatedUser.getRole());
                LogDAO logDAO = new LogDAO(manager.getConnection());
                logDAO.createLog(log);
                session.setAttribute("welcomeMessage", "Profile updated successfully.");
                response.sendRedirect("welcome.jsp");
            } else {
                session.setAttribute("dbError", "Failed to update profile. Please try again.");
                request.getRequestDispatcher("edit.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating profile", ex);
            session.setAttribute("dbError", "Database error occurred. Please try again.");
            request.getRequestDispatcher("edit.jsp").forward(request, response);
        }
    }
}
