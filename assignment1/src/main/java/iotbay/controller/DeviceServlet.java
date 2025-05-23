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

        try {
            DBConnector dbConnector = new DBConnector();
            Connection conn = dbConnector.openConnection();

            DeviceDAO dao = new DeviceDAO(conn);
            List<Device> devices = dao.getAllDevices();


            request.setAttribute("devices", devices);
            request.getRequestDispatcher("products.jsp").forward(request, response);
            dbConnector.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Error accessing database.", e);
        }
    }
}
