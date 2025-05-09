package iotbay.controller;

import iotbay.dao.UserDAO;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/RegisterServlet")
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

        // Basic validation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Create User object
        User user = new User(firstName, lastName, email, confirmPassword, phone);

        // Save to database
        UserDAO userDAO = new UserDAO();
        boolean isRegistered = userDAO.registerUser(user);

        if (isRegistered) {
            // Success: Redirect to welcome page
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            response.sendRedirect("welcome.jsp");
        } else {
            // Failure: Redirect back to register page with error
            request.setAttribute("errorMessage", "Failed to register. Try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}