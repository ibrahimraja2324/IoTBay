package iotbay.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import iotbay.dao.LogDAO;
import iotbay.model.Log;
import iotbay.model.User;
import iotbay.dao.DBManager;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        DBManager manager = (DBManager) session.getAttribute("manager");
        LogDAO logDAO = new LogDAO(manager.getConnection());

        if (session != null || manager != null) {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser != null || logDAO != null) {
                Log log = new Log(currentUser.getEmail(), "Logout", currentUser.getRole());
                try {
                    logDAO.createLog(log);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            session.removeAttribute("currentUser");
        }
        response.sendRedirect("logout.jsp"); // Redirect to a logout confirmation page
    }
}