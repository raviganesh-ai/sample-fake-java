package com.example.library.dao;

import com.example.library.model.Loan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Plain JDBC data-access object for Loan records. A Spring Boot
 * migration would typically replace this with a Spring Data JPA
 * LoanRepository interface.
 */
public class LoanDao {

    public List<Loan> findAll() throws SQLException {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loan ORDER BY id";
        List<Loan> loans = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                loans.add(mapRow(resultSet));
            }
        }
        return loans;
    }

    public List<Loan> findActiveLoans() throws SQLException {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date "
                + "FROM loan WHERE return_date IS NULL ORDER BY due_date";
        List<Loan> loans = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                loans.add(mapRow(resultSet));
            }
        }
        return loans;
    }

    public Optional<Loan> findById(int id) throws SQLException {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date FROM loan WHERE id = ?";
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

    public int insert(Loan loan) throws SQLException {
        String sql = "INSERT INTO loan (book_id, member_id, loan_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, loan.getBookId());
            statement.setInt(2, loan.getMemberId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, Date.valueOf(loan.getDueDate()));
            statement.setDate(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert loan, no generated key returned");
    }

    public boolean markReturned(int id, LocalDate returnDate) throws SQLException {
        String sql = "UPDATE loan SET return_date = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(returnDate));
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Loan mapRow(ResultSet resultSet) throws SQLException {
        Date returnDate = resultSet.getDate("return_date");
        return new Loan(
                resultSet.getInt("id"),
                resultSet.getInt("book_id"),
                resultSet.getInt("member_id"),
                resultSet.getDate("loan_date").toLocalDate(),
                resultSet.getDate("due_date").toLocalDate(),
                returnDate != null ? returnDate.toLocalDate() : null
        );
    }
}
