package iotbay.controller;

import iotbay.dao.DBConnector;
import iotbay.dao.DeviceDAO;
import iotbay.model.Device;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/devices")
public class DeviceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Connection conn = null;
        DBConnector db = null;
        try {
            db = new DBConnector();
            conn = db.openConnection();
            DeviceDAO dao = new DeviceDAO(conn);

            if (action == null || action.equals("list")) {
                List<Device> devices = dao.getAllDevices();
                request.setAttribute("devices", devices);
                request.getRequestDispatcher("products.jsp").forward(request, response);

            } else if ("addNewDevice".equals(action)) {
                request.getRequestDispatcher("add-device.jsp").forward(request, response);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Device device = dao.getDeviceById(id);
                request.setAttribute("device", device);
                request.getRequestDispatcher("edit-device.jsp").forward(request, response);

            } else if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                String type = request.getParameter("type");
                List<Device> devices = dao.searchDevices(keyword, type);
                request.setAttribute("devices", devices);
                request.getRequestDispatcher("products.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Error accessing database.", e);
        } finally {
            if (db != null) {
                try {
                    db.closeConnection();
                } catch (SQLException ignored) {}
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Connection conn = null;
        DBConnector db = null;
        try {
            db = new DBConnector();
            conn = db.openConnection();
            DeviceDAO dao = new DeviceDAO(conn);

            if ("addDevice".equals(action)) {
                String name = request.getParameter("name");
                String type = request.getParameter("type");
                double price = Double.parseDouble(request.getParameter("unitPrice"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                Device device = new Device(0, name, type, price, quantity);
                dao.addDevice(device);
                response.sendRedirect("add-device.jsp?success=true");

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteDevice(id);
                response.sendRedirect("devices?action=list");

            } else if ("updateDevice".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String type = request.getParameter("type");
                double price = Double.parseDouble(request.getParameter("unitPrice"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                Device device = new Device(id, name, type, price, quantity);
                boolean updated = dao.updateDevice(device);

                if (updated) {
                    response.sendRedirect("devices?action=edit&id=" + id + "&success=true");
                } else {
                    response.sendRedirect("edit-device.jsp?error=true&id=" + id);
                }
            }

        } catch (Exception e) {
            throw new ServletException("Error processing POST request", e);
        } finally {
            if (db != null) {
                try {
                    db.closeConnection();
                } catch (SQLException ignored) {}
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
