package iotbay.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import iotbay.dao.DBConnector;
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

public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        
        // Initialize DBManager if not present
        if (manager == null) {
            try {
                DBConnector db = new DBConnector();
                manager = new DBManager(db.openConnection());
                session.setAttribute("manager", manager);
            } catch (ClassNotFoundException | SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error initializing DBManager", ex);
                session.setAttribute("loginError", "Database connection error. Please try again later.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
        }
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        session.setAttribute("emailError", null);
        session.setAttribute("passwordError", null);
        session.setAttribute("loginError", null);
        
        Validator validator = new Validator();
        
        if (!validator.validateEmail(email)) {
            session.setAttribute("emailError", "Invalid email format.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        if (!validator.validatePassword(password)) {
            session.setAttribute("passwordError", "Invalid password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        User user = null;
        try {
            user = manager.findUser(email, password);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding user", ex);
        }
        
        if (user != null) {
            Log log = new Log(email, "Login", user.getRole());
            LogDAO logDAO = new LogDAO(manager.getConnection());
            try {
                logDAO.createLog(log);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error creating login log", ex);
            }
            session.setAttribute("currentUser", user);
            response.sendRedirect("main.jsp");
        } else {
            // Check if the user exists but is deactivated
            UserDAO userDAO = new UserDAO(manager.getConnection());
            try {
                User deactivatedUser = userDAO.findUser(email);
                if (deactivatedUser != null && !deactivatedUser.isActive()) {
                    session.setAttribute("loginError", "This account has been deactivated. Please contact support.");
                } else {
                    session.setAttribute("loginError", "Invalid email or password.");
                }
            } catch (SQLException ex) {
                session.setAttribute("loginError", "Invalid email or password.");
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
