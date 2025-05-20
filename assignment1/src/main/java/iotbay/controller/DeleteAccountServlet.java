package iotbay.controller;

import iotbay.dao.UserDAO;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import iotbay.dao.DBManager;
import iotbay.model.Log;
import iotbay.dao.LogDAO;


public class DeleteAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
        }

        UserDAO userDAO = new UserDAO(manager.getConnection());
        User currentUser = (User) session.getAttribute("currentUser");
        String email = currentUser.getEmail();
        
        try {
            // Delete the user from the database
            userDAO.deleteUser(email);

            // Log the deletion
            Log log = new Log(email, "Account Deletion", currentUser.getRole());
            LogDAO logDAO = new LogDAO(manager.getConnection());
            logDAO.createLog(log);

            // Invalidate the session and redirect to the home page
            session.invalidate();
            response.sendRedirect("index.jsp");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }

    }
}