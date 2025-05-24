package iotbay.controller;

import java.io.IOException;

import iotbay.model.Cart;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/removeFromCart")
public class RemoveFromCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        int deviceId = Integer.parseInt(request.getParameter("deviceId"));

        if (cart != null) {
            cart.removeItem(deviceId);
        }

        response.sendRedirect("cart.jsp");
    }
}