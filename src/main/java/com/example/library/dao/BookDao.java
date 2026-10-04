package com.example.library.dao;

import com.example.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Plain JDBC data-access object for Book records. A Spring Boot
 * migration would typically replace this with a Spring Data JPA
 * BookRepository interface.
 */
public class BookDao {

    public List<Book> findAll() throws SQLException {
        String sql = "SELECT id, title, author, isbn, available FROM book ORDER BY id";
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(mapRow(resultSet));
            }
        }
        return books;
    }

    public Optional<Book> findById(int id) throws SQLException {
        String sql = "SELECT id, title, author, isbn, available FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public int insert(Book book) throws SQLException {
        String sql = "INSERT INTO book (title, author, isbn, available) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setBoolean(4, book.isAvailable());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert book, no generated key returned");
    }

    public boolean updateAvailability(int id, boolean available) throws SQLException {
        String sql = "UPDATE book SET available = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Book mapRow(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                resultSet.getBoolean("available")
        );
    }
}
