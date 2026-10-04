package com.example.library.servlet;

import com.example.library.dao.MemberDao;
import com.example.library.model.Member;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Classic Java EE Servlet controller for Member resources, registered
 * in web.xml.
 */
public class MemberServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Member> members = memberDao.findAll();
            request.setAttribute("members", members);
            request.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load members", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String membershipLevel = request.getParameter("membershipLevel");
        Member member = new Member(
                0,
                fullName,
                email,
                (membershipLevel == null || membershipLevel.isEmpty()) ? "STANDARD" : membershipLevel
        );
        try {
            memberDao.insert(member);
        } catch (SQLException e) {
            throw new ServletException("Failed to create member", e);
        }
        response.sendRedirect(request.getContextPath() + "/members");
    }
}
