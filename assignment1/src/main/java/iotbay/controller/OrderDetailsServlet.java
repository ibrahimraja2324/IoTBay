// package iotbay.controller;

// import java.io.IOException;
// import java.sql.Connection;
// import java.sql.SQLException;

// import iotbay.dao.DBConnector;
// import iotbay.dao.OrderDAO;
// import iotbay.model.Order;
// import iotbay.model.User;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// @WebServlet("/orderDetails")
// public class OrderDetailsServlet extends HttpServlet {
//     private DBConnector db;
//     private OrderDAO orderDAO;

//     @Override
//     public void init() throws ServletException {
//         try {
//             db = new DBConnector();
//             Connection conn = db.openConnection();
//             orderDAO = new OrderDAO(conn);
//         } catch (ClassNotFoundException | SQLException e) {
//             throw new ServletException("DBConnector initialization failed", e);
//         }
//     }

//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {

//         HttpSession session = request.getSession(false);
//         User user = (User) (session != null ? session.getAttribute("user") : null);

//         if (user == null) {
//             response.sendRedirect("login.jsp");
//             return;
//         }

//         String orderIdParam = request.getParameter("orderId");
//         if (orderIdParam == null) {
//             response.sendRedirect("listOrders");
//             return;
//         }

//         try {
//             int orderId = Integer.parseInt(orderIdParam);
//             Order order = (Order) orderDAO.getOrder(orderId);

//             if (order == null || !order.getUserEmail().equals(user.getEmail())) {
//                 response.sendRedirect("listOrders");
//                 return;
//             }

//             request.setAttribute("order", order);
//             request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.sendRedirect("listOrders");
//         }
//     }

//     @Override
//     public void destroy() {
//         try {
//             if (db != null) db.closeConnection();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }