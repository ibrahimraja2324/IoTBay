package iotbay.controller;

import java.io.IOException;
import java.util.List;
import iotbay.dao.DBManager;
import iotbay.dao.LogDAO;
import iotbay.model.Log;
import iotbay.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class LogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            session.setAttribute("logError", "DBManager not initialized. Please navigate from the home page.");
        }

        User currentUser = (User) session.getAttribute("currentUser");

        LogDAO logDAO = new LogDAO(manager.getConnection());
        String searchDate = request.getParameter("searchDate");
        String dateFilter = request.getParameter("dateFilter");
        List<Log> logs = null;

        try {
            if (currentUser != null) {
                if (currentUser.getRole().equals("USER")) {
                    logs = logDAO.getLogsByEmail(currentUser.getEmail());
                    logs = logDAO.filterLogsByDate(logs, searchDate, dateFilter);
                } else {
                    logs = logDAO.getAllLogsAsList();
                    logs = logDAO.filterLogsByDate(logs, searchDate, dateFilter);
                }
            } else {
                session.setAttribute("logError", "User not logged in.");
            }
            
            request.setAttribute("logs", logs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("log.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            session.setAttribute("logError", "Error retrieving logs: " + e.getMessage());
        }

        
    }
}
