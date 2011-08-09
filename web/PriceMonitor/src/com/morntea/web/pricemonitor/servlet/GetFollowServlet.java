package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.morntea.web.pricemonitor.ErrorCode;
import com.morntea.web.pricemonitor.data.User;

public class GetFollowServlet extends HttpServlet {
    
    private static final long serialVersionUID = 4838353554975226298L;

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter respWriter = resp.getWriter();
        
        HttpSession session = req.getSession(false);
        
        // user did not log in
        if (session == null) {
        	respWriter.print("ErrorCode=" + ErrorCode.GETFOLLOW_USER_NOT_LOGIN);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	respWriter.print("ErrorCode=" + ErrorCode.GETFOLLOW_USER_NOT_LOGIN);
            return;
        }
    }
}
