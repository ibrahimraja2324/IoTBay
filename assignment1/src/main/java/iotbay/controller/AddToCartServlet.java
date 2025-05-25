package iotbay.controller;

import java.io.IOException;
import java.sql.SQLException;

import iotbay.dao.DeviceDAO;
import iotbay.model.Cart;
import iotbay.model.CartItem;
import iotbay.model.Device;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    private DeviceDAO deviceDAO;

    @Override
    public void init() throws ServletException {
        deviceDAO = new DeviceDAO(null); // Assuming DeviceDAO has a default constructor
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int deviceId = Integer.parseInt(request.getParameter("deviceId"));

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        try {
            Device device = deviceDAO.getDeviceById(deviceId);
            if (device != null) {
                boolean found = false;
                for (CartItem item : cart.getItems()) {
                    if (item.getDevice().getId() == deviceId) {
                        item.setQuantity(item.getQuantity() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cart.addItem(new CartItem(device, 1));
                }
                session.setAttribute("cart", cart);
            }
        } catch (SQLException e) {
            throw new ServletException("Failed to fetch device", e);
        }

        response.sendRedirect("cart.jsp");
    }
}
