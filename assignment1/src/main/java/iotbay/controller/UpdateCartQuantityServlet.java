package iotbay.controller;

import java.io.IOException;
import java.util.List;

import iotbay.model.Cart;
import iotbay.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateQuantity")
public class UpdateCartQuantityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendRedirect("cart.jsp");
            return;
        }

        try {
            int deviceId = Integer.parseInt(request.getParameter("deviceId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity < 1) {
                session.setAttribute("checkoutError", "Quantity must be at least 1.");
                response.sendRedirect("cart.jsp");
                return;
            }

            List<CartItem> items = cart.getItems();
            for (CartItem item : items) {
                if (item.getDevice().getId() == deviceId) {
                    item.setQuantity(quantity);
                    break;
                }
            }

            session.setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            session.setAttribute("checkoutError", "Invalid input format.");
        }

        response.sendRedirect("cart.jsp");
    }
}