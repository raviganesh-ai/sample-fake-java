package com.example.library.dao;

import com.example.library.model.Book;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookDaoTest {

    private final BookDao bookDao = new BookDao();

    @Before
    public void setUp() throws SQLException {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS book ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "author VARCHAR(255) NOT NULL, "
                    + "isbn VARCHAR(32) NOT NULL, "
                    + "available BOOLEAN NOT NULL DEFAULT TRUE)");
            statement.execute("DELETE FROM book");
        }
    }

    @Test
    public void insertAndFindById() throws SQLException {
        Book book = new Book(0, "Effective Java", "Joshua Bloch", "978-0134685991", true);
        int id = bookDao.insert(book);

        Optional<Book> found = bookDao.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().getTitle());
    }

    @Test
    public void findAllReturnsInsertedBooks() throws SQLException {
        bookDao.insert(new Book(0, "Clean Code", "Robert C. Martin", "978-0132350884", true));
        bookDao.insert(new Book(0, "Spring in Action", "Craig Walls", "978-1617297571", true));

        List<Book> books = bookDao.findAll();

        assertEquals(2, books.size());
    }

    @Test
    public void updateAvailabilityChangesFlag() throws SQLException {
        int id = bookDao.insert(new Book(0, "Domain-Driven Design", "Eric Evans", "978-0321125217", true));

        boolean updated = bookDao.updateAvailability(id, false);

        assertTrue(updated);
        Optional<Book> found = bookDao.findById(id);
        assertTrue(found.isPresent());
        assertFalse(found.get().isAvailable());
    }

    @Test
    public void deleteRemovesBook() throws SQLException {
        int id = bookDao.insert(new Book(0, "Refactoring", "Martin Fowler", "978-0134757599", true));

        boolean deleted = bookDao.delete(id);

        assertTrue(deleted);
        assertFalse(bookDao.findById(id).isPresent());
    }
}
