package com.example.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Plain JDBC connection manager backed by an in-memory H2 database.
 * A Spring Boot migration would typically replace this with a
 * configured DataSource bean plus Spring Data JPA repositories.
 */
public final class DatabaseConnectionManager {

    private static final String JDBC_URL = "jdbc:h2:mem:library;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private DatabaseConnectionManager() {
    }

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("H2 JDBC driver not found on the classpath", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}
