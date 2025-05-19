package iotbay.controller;

import iotbay.dao.DBManager;
import iotbay.dao.OrderDAO;
import iotbay.model.Order;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderHistoryServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
        }
        try {
            listOrders(request, response, manager);
        } catch (SQLException ex) {
            Logger.getLogger(OrderHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    // Use OrderDAO to load orders for the current user.
    private void listOrders(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        Connection conn = manager.getConnection();
        OrderDAO orderDAO = new OrderDAO(conn);
        List<Order> orders = orderDAO.findOrdersByUser(currentUser.getUserID());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("order-history.jsp").forward(request, response);
    }
}
