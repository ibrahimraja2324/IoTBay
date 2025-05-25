package iotbay.controller;

import java.io.IOException;
import java.sql.SQLException;

import iotbay.dao.LogDAO;
import iotbay.dao.UserDAO;
import iotbay.model.Log;
import iotbay.model.User;

import iotbay.dao.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GuestLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");

        if (manager == null) {
            throw new ServletException("DBManager not initialized.");
        }

        UserDAO userDAO = new UserDAO(manager.getConnection());

        try {
            User guest = userDAO.findUser("guest@iotbay.com", "guestpassword");
            if (guest != null) {
                userDAO.deleteUser(guest.getEmail());
            }
            guest = new User("Guest", "User", "guest@iotbay.com", "guestpassword", "0000000000", "GUEST");
            userDAO.registerUser(guest);

            session.setAttribute("currentUser", guest);

            Log log = new Log(guest.getEmail(), "Guest Login", guest.getRole());
            LogDAO logDAO = new LogDAO(manager.getConnection());
            logDAO.createLog(log);

            response.sendRedirect("main.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error during guest login", e);
        }
    }
}