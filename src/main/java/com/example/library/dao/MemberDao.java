package com.example.library.dao;

import com.example.library.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Plain JDBC data-access object for Member records. A Spring Boot
 * migration would typically replace this with a Spring Data JPA
 * MemberRepository interface.
 */
public class MemberDao {

    public List<Member> findAll() throws SQLException {
        String sql = "SELECT id, full_name, email, membership_level FROM member ORDER BY id";
        List<Member> members = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                members.add(mapRow(resultSet));
            }
        }
        return members;
    }

    public Optional<Member> findById(int id) throws SQLException {
        String sql = "SELECT id, full_name, email, membership_level FROM member WHERE id = ?";
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

    public int insert(Member member) throws SQLException {
        String sql = "INSERT INTO member (full_name, email, membership_level) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, member.getFullName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getMembershipLevel());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert member, no generated key returned");
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM member WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Member mapRow(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getInt("id"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("membership_level")
        );
    }
}
