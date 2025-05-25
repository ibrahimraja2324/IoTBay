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
        // Normally, you would inject a DB connection here
        deviceDAO = new DeviceDAO(null); // Ensure DeviceDAO handles null safely or update to inject connection
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String deviceIdParam = request.getParameter("deviceId");
        if (deviceIdParam == null || deviceIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Device ID is missing.");
            return;
        }

        int deviceId;
        try {
            deviceId = Integer.parseInt(deviceIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid device ID format.");
            return;
        }

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
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Device not found.");
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while adding device to cart", e);
        }

        response.sendRedirect("cart.jsp");
    }
}