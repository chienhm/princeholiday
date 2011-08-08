package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.morntea.web.pricemonitor.Page;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1488277712415421152L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.setAttribute("user", null);
        }

        Cookie usernameCookie = new Cookie("username", null);
        resp.addCookie(usernameCookie);
        resp.sendRedirect(Page.LOGIN);
    }

}
