package com.example.library.servlet;

import com.example.library.dao.BookDao;
import com.example.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Classic Java EE Servlet controller for Book resources, registered in
 * web.xml. A Spring Boot migration would typically replace this with a
 * Controller or RestController class.
 */
public class BookServlet extends HttpServlet {

    private final BookDao bookDao = new BookDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Book> books = bookDao.findAll();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load books", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        Book book = new Book(0, title, author, isbn, true);
        try {
            bookDao.insert(book);
        } catch (SQLException e) {
            throw new ServletException("Failed to create book", e);
        }
        response.sendRedirect(request.getContextPath() + "/books");
    }
}
