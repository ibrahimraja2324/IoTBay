package iotbay.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("currentUser") != null);

        if (!loggedIn) {
            ((HttpServletResponse) response).sendRedirect("login.jsp?error=Please+login+to+access+this+page");
        } else {
            chain.doFilter(request, response);
        }
    }
} 