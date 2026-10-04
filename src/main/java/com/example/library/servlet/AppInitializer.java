package com.example.library.servlet;

import com.example.library.dao.DatabaseConnectionManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classic Java EE application lifecycle hook, registered in web.xml.
 * Creates the schema and seeds demo data when the web application
 * starts up. A Spring Boot migration would typically replace this
 * with schema.sql/data.sql auto-initialization or a CommandLineRunner
 * bean.
 */
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS book ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "author VARCHAR(255) NOT NULL, "
                    + "isbn VARCHAR(32) NOT NULL, "
                    + "available BOOLEAN NOT NULL DEFAULT TRUE)");
            statement.execute("CREATE TABLE IF NOT EXISTS member ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "full_name VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) NOT NULL, "
                    + "membership_level VARCHAR(32) NOT NULL DEFAULT 'STANDARD')");
            statement.execute("CREATE TABLE IF NOT EXISTS loan ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "book_id INT NOT NULL, "
                    + "member_id INT NOT NULL, "
                    + "loan_date DATE NOT NULL, "
                    + "due_date DATE NOT NULL, "
                    + "return_date DATE)");
            statement.execute("INSERT INTO book (title, author, isbn, available) VALUES "
                    + "('Effective Java', 'Joshua Bloch', '978-0134685991', TRUE), "
                    + "('Clean Code', 'Robert C. Martin', '978-0132350884', TRUE), "
                    + "('Spring in Action', 'Craig Walls', '978-1617297571', TRUE)");
            statement.execute("INSERT INTO member (full_name, email, membership_level) VALUES "
                    + "('Ada Lovelace', 'ada@example.com', 'PREMIUM'), "
                    + "('Grace Hopper', 'grace@example.com', 'STANDARD')");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize the library database schema", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No explicit teardown required for the in-memory H2 database.
    }
}
