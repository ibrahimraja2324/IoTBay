package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import iotbay.dao.CartItemDAO;
import iotbay.dao.DBManager;
import iotbay.dao.DeviceDAO;
import iotbay.dao.LogDAO;
import iotbay.model.Cart;
import iotbay.model.CartItem;
import iotbay.model.Log;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
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
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (user != null) {
            Log log = new Log(email, "Login", user.getRole());
            LogDAO logDAO = new LogDAO(manager.getConnection());
            try {
                logDAO.createLog(log);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            session.setAttribute("currentUser", user);

            try {
                Connection conn = manager.getConnection();
                CartItemDAO cartItemDAO = new CartItemDAO(conn);
                DeviceDAO deviceDAO = new DeviceDAO(conn);

                List<CartItem> items = cartItemDAO.getCartItems(user.getEmail(), deviceDAO);
                Cart cart = new Cart(items);
                session.setAttribute("cart", cart);
            } catch (SQLException e) {
                e.printStackTrace(); 
            }

            response.sendRedirect("main.jsp");
        } else {
            session.setAttribute("loginError", "User does not exist.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
