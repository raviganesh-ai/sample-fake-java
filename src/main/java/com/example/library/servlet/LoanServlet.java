package com.example.library.servlet;

import com.example.library.dao.LoanDao;
import com.example.library.model.Loan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classic Java EE Servlet controller for Loan resources, registered in
 * web.xml.
 */
public class LoanServlet extends HttpServlet {

    private final LoanDao loanDao = new LoanDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Loan> loans = loanDao.findAll();
            request.setAttribute("loans", loans);
            request.getRequestDispatcher("/WEB-INF/views/loans.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load loans", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("return".equals(action)) {
                int loanId = Integer.parseInt(request.getParameter("loanId"));
                loanDao.markReturned(loanId, LocalDate.now());
            } else {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int memberId = Integer.parseInt(request.getParameter("memberId"));
                Loan loan = new Loan(0, bookId, memberId, LocalDate.now(), LocalDate.now().plusDays(14), null);
                loanDao.insert(loan);
            }
        } catch (SQLException e) {
            throw new ServletException("Failed to process loan request", e);
        }
        response.sendRedirect(request.getContextPath() + "/loans");
    }
}
