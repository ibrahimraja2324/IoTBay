package iotbay.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import iotbay.dao.DBManager;
import iotbay.dao.UserDAO;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DBManager manager = (DBManager) session.getAttribute("manager");
    
        if (manager == null) {
            throw new ServletException("DBManager not initialized.");
        }
    
        userDAO = new UserDAO(manager.getConnection());
    
        String action = request.getParameter("action");
        if (action == null) action = "";
    
        try {
            switch (action) {
                case "edit" -> showEditForm(request, response);
                case "delete" -> deleteUser(request, response);
                case "toggleStatus" -> toggleUserStatus(request, response);
                default -> listUsers(request, response);
            }
        } catch (SQLException | ServletException | IOException e) {
            LOGGER.severe(() -> "Error processing request: " + e.getMessage());
            response.getWriter().println("An error occurred: " + e.getMessage());
        }
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DBManager manager = (DBManager) session.getAttribute("manager");
    
        if (manager == null) {
            throw new ServletException("DBManager not initialized.");
        }
    
        userDAO = new UserDAO(manager.getConnection());
    
        String action = request.getParameter("action");
    
        try {
            switch (action) {
                case "create" -> createUser(request, response);
                case "update" -> updateUser(request, response);
                default -> response.sendRedirect("viewuser.jsp");
            }
        } catch (SQLException | IOException e) {
            LOGGER.severe(() -> "Error processing request: " + e.getMessage());
            response.getWriter().println("An error occurred: " + e.getMessage());
        }
        
    }
    

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> users = userDAO.getAllUsers();
        request.getSession().setAttribute("allUsers", users);
        response.sendRedirect("viewuser.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ServletException, IOException {
    String email = request.getParameter("email");
    LOGGER.info(() -> "Entered showEditForm with email: " + email);

    if (email == null || email.trim().isEmpty()) {
        LOGGER.warning("Email parameter is missing or empty.");
        response.sendRedirect("viewuser.jsp");
        return;
    }

    User existingUser = userDAO.findUser(email);
    LOGGER.info(() -> "Found user: " + (existingUser != null ? existingUser.getEmail() : "null"));

    if (existingUser != null) {
        request.setAttribute("user", existingUser);
        request.getRequestDispatcher("editUser.jsp").forward(request, response);
    } else {
        request.setAttribute("error", "No user data found.");
        request.getRequestDispatcher("editUser.jsp").forward(request, response);
    }
}



    private void createUser(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        
        if (userDAO.findUser(email) != null) {
            response.getWriter().println("User with this email already exists.");
            return;
        }

    User newUser = new User(firstName, lastName, email, password, phone, role);
    userDAO.registerUser(newUser);
    response.sendRedirect("UserServlet");
}


    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");

        User user = new User(firstName, lastName, email, password, phone, null);
        userDAO.updateUser(user);
        response.sendRedirect("UserServlet");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        
        if (userDAO.deleteUser(email)) {
            // If the user deleted their own account
            session.invalidate(); // Clear the session
            response.sendRedirect("main.jsp?error=Account deleted successfully. Please login with a different account.");
        } else {
            response.sendRedirect("UserServlet?error=Failed to delete user");
        }
    }

    private void toggleUserStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        
        if (userDAO.toggleUserStatus(email)) {
            // If the user deactivated their own account
            session.invalidate(); // Clear the session
            response.sendRedirect("main.jsp?error=Account deactivated. Please contact an administrator to reactivate your account.");
        } else {
            response.sendRedirect("UserServlet?error=Failed to update user status");
        }
    }

}
