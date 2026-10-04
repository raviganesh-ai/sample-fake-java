package com.example.library.dao;

import com.example.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = {"com.example.library.dao"})
public class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void setUp() {
        // ensure clean state if needed; DataJpaTest starts with empty in-memory DB by default
    }

    @Test
    public void testAddAndGetAllBooks() {
        Book book = new Book("Test Title", "Test Author", "1234567890");
        bookDao.addBook(book);

        List<Book> books = bookDao.getAllBooks();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals("Test Title", books.get(0).getTitle());
    }
}
