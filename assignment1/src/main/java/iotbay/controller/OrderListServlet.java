package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import iotbay.dao.DBConnector;
import iotbay.dao.OrderDAO;
import iotbay.model.Order;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/listOrders")
public class OrderListServlet extends HttpServlet {
    private DBConnector db;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        try {
            db = new DBConnector();
            Connection conn = db.openConnection();
            orderDAO = new OrderDAO(conn);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("DBConnector initialization failed.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = user.getEmail();
        String currentSortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "orderId";
        String currentSortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "asc";
        String searchType = request.getParameter("searchType");
        String searchTerm = request.getParameter("searchTerm");

        try {
            List<Order> orders = orderDAO.findOrdersByUser(email);

            // 필터링
            if (searchType != null && searchTerm != null && !searchTerm.trim().isEmpty()) {
                orders = orders.stream()
                        .filter(o -> {
                            if ("orderId".equalsIgnoreCase(searchType)) {
                                return String.valueOf(o.getOrderId()).contains(searchTerm);
                            } else if ("orderDate".equalsIgnoreCase(searchType)) {
                                return o.getOrderDate() != null && o.getOrderDate().contains(searchTerm);
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
            }

            orders = orders.stream()
                    .sorted((o1, o2) -> {
                        int cmp;
                        if ("orderId".equalsIgnoreCase(currentSortBy)) {
                            cmp = Integer.compare(o1.getOrderId(), o2.getOrderId());
                        } else {
                            cmp = String.valueOf(o1.getOrderDate()).compareTo(String.valueOf(o2.getOrderDate()));
                        }
                        return "asc".equalsIgnoreCase(currentSortOrder) ? cmp : -cmp;
                    })
                    .collect(Collectors.toList());

            request.setAttribute("orderList", orders);
            request.setAttribute("currentSortBy", currentSortBy);
            request.setAttribute("currentSortOrder", currentSortOrder);
            request.getRequestDispatcher("orderlist.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error retrieving orders for user " + email, e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (db != null) {
                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}