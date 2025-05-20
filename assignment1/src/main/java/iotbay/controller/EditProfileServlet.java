package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Handle POST request
        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String currentPassword = request.getParameter("currentPassword");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        User currentUser = (User) session.getAttribute("currentUser");
        String email = currentUser.getEmail();

        // Clear any previous error messages from the session
        session.setAttribute("editError", null);
        session.setAttribute("passwordError", null);
        session.setAttribute("dbError", null);

        // Validate and update user profile logic here
        Validator validator = new Validator();
        boolean hasError = false;

        // Make sure that email is actually in the db
        // Retrieve the DBManager from the session
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            session.setAttribute("dbError", "Database connection not available. Please try again later.");
            hasError = true;
        }
        Connection conn = manager.getConnection();
        UserDAO userDAO = new UserDAO(conn);


        // Check if password is correct
        if (!currentUser.getPassword().equals(currentPassword)) {
            session.setAttribute("editError", "Current password is incorrect.");
            hasError = true;
        }

        // Check if password is empty (meaning no change)
        if (confirmPassword == "" && password == "") {
            password = currentUser.getPassword();
        } else {
            // Validate all inputs and collect all error messages
            if (!password.equals(confirmPassword)) {
                session.setAttribute("passwordError", "Passwords do not match.");
                hasError = true;
            }

            // (must be at least 4 characters and contain only lowercase letters and digits)
            if (!validator.validatePassword(password)) {
                session.setAttribute("passwordError", "Password must be at least 4 characters.");
                hasError = true;
            }
        }
        
        // If any validation error, forward to register.jsp and return
        if (hasError) {
            request.getRequestDispatcher("edit.jsp").forward(request, response);
            return;
        }

        User user = new User(firstName, lastName, email, password, phone, "USER");

        try {
            // Update user in the database
            userDAO.updateUser(user);
            session.setAttribute("currentUser", user);
            Log log = new Log(email, "Profile edited", user.getRole());
            LogDAO logDAO = new LogDAO(conn);
            logDAO.createLog(log);
            session.setAttribute("welcomeMessage", "Profile updated successfully.");
            response.sendRedirect("welcome.jsp");
            return;
        } catch (Exception e) {
            session.setAttribute("dbError", "Database error: " + e.getMessage());
            request.getRequestDispatcher("edit.jsp").forward(request, response);
            return;
        }
    }
    
}
