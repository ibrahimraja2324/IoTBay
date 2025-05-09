package iotbay.controller;

import iotbay.dao.UserDAO;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve email and password from the login form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate credentials using UserDAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(email, password);
        
        if (user != null) {
            // Create session and store user details
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            // Redirect to the main page or dashboard
            response.sendRedirect("welcome.jsp");
        } else {
            // If login fails, set an error attribute and forward back to login page
            request.setAttribute("errorMessage", "Invalid email or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }


}