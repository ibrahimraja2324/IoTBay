package iotbay.controller;

import iotbay.dao.DBManager;
import iotbay.dao.UserDAO;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");

        HttpSession session = request.getSession();
        // Clear any previous error messages from the session
        session.setAttribute("registerError", null);
        session.setAttribute("emailError", null);
        session.setAttribute("passwordError", null);
        session.setAttribute("nameError", null);

        // Instantiate Validator (assumes you have a Validator class)
        Validator validator = new Validator();
        

        boolean hasError = false;

        // Validate all inputs and collect all error messages
        if (!password.equals(confirmPassword)) {
            session.setAttribute("registerError", "Passwords do not match.");
            hasError = true;
        }

        if (!validator.validateEmail(email)) {
            session.setAttribute("emailError", "Invalid email format.");
            hasError = true;
        }

        // (must be at least 4 characters and contain only lowercase letters and digits)
        if (!validator.validatePassword(password)) {
            session.setAttribute("passwordError", "Password must be at least 4 characters.");
            hasError = true;
        }

        // Retrieve the DBManager from the session
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            session.setAttribute("registerError", "Database connection not available. Please try again later.");
            hasError = true;
        }

        // If any validation error, forward to register.jsp and return
        if (hasError) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Create User object using the validated values
        User user = new User(firstName, lastName, email, password, phone);

        // Get the underlying connection from the DBManager and instantiate UserDAO
        Connection conn = manager.getConnection();
        UserDAO userDAO = new UserDAO(conn);

        // Check if the email already exists in the database
        boolean isRegistered = false;
        try {
            User existingUser = userDAO.findUser(email);
            if (existingUser != null) {
                session.setAttribute("emailError", "Email is already in use. Try logging in instead.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            isRegistered = userDAO.registerUser(user);
        } catch (SQLException ex) {
            ex.printStackTrace();
            session.setAttribute("registerError", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (isRegistered) {
            // Registration successful: set the user in the session and redirect to welcome page
            session.setAttribute("currentUser", user);
            response.sendRedirect("welcome.jsp");
        } else {
            // Registration failed: set an error message (for example, duplicate email)
            session.setAttribute("registerError", "Failed to register. Possibly the email is already in use.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
