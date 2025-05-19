package iotbay.controller;

import java.io.IOException;
import java.util.List;
import iotbay.dao.DBManager;
import iotbay.dao.LogDAO;
import iotbay.model.Log;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class LogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
        }

        LogDAO logDAO = new LogDAO(manager.getConnection());

        try {
            List<Log> logs = logDAO.getAllLogsAsList();
            request.setAttribute("logs", logs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("log.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error retrieving logs", e);
        }

        
    }
}
