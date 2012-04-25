package com.morntea.web.family;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberServlet extends HttpServlet {

	private static final long serialVersionUID = 8074349473325877818L;
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        String _id = req.getParameter("id");
        String action = req.getParameter("action");
        Long id = -1L;
        if(action!=null) {
            MemberService ms = new MemberService();
            if(action.equalsIgnoreCase("del")) {
                if(_id!=null) {
                    id = Long.parseLong(_id);
                    ms.delMember(id);
                }
            } else if(action.equalsIgnoreCase("init")) {
                //ms.addMember("娄XX","1","4","");
            }
        }
        resp.sendRedirect("/family/list.jsp");
    }
    
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		
        String _id = req.getParameter("id");
        String _fatherId = req.getParameter("fid");
        String _gender = req.getParameter("gender");
        String _zipai = req.getParameter("gen");
        String _birthday = req.getParameter("birthday");
        String _lunarbirthday = req.getParameter("lunar");
        String _deathday = req.getParameter("deathday");
        
		Date birthday = null;
		Date lunarbirthday = null;
        Date deathday = null;
        try {
            if(_birthday!=null && !_birthday.isEmpty()) {
                birthday = new SimpleDateFormat("yyyy-MM-dd").parse(_birthday);
            }
            if(_lunarbirthday!=null && !_lunarbirthday.isEmpty()) {
                lunarbirthday = new SimpleDateFormat("yyyy-MM-dd").parse(_lunarbirthday);
            }
            if(_deathday!=null && !_deathday.isEmpty()) {
                deathday = new SimpleDateFormat("yyyy-MM-dd").parse(_deathday);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }        
        Long id = -1L;
		if(_id!=null) {
		    id = Long.parseLong(_id);
		}
		Long fatherId = -1L;
		if(_fatherId!=null) {
		    fatherId = Long.parseLong(_fatherId);
		}
		String name = req.getParameter("name");
		boolean gender = false;
		if(_gender!=null) {
		    gender = _gender.equals("1");
		}
		int zipai = -1;
		if(_zipai!=null) {
		    zipai = Integer.parseInt(_zipai);
		}
		String motherName = req.getParameter("mother");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
		String comment = req.getParameter("comment");
		
		String action = req.getParameter("action");
		
        MemberService ms = new MemberService();
		if(action.equalsIgnoreCase("update")) {
		    if(id!=-1L){
		        ms.updateMember(id, fatherId, name, birthday, lunarbirthday, deathday, gender, zipai, motherName, phone, address, comment);
		    }
		} else if (action.equalsIgnoreCase("add")) {
            ms.addMember(fatherId, name, birthday, lunarbirthday, deathday, gender, zipai, motherName, phone, address, comment);
		}
		resp.sendRedirect("/family/list.jsp");
	}
}
