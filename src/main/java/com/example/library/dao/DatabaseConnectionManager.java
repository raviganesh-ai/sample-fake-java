package com.example.library.dao;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Legacy placeholder to preserve type references; actual connection management is now handled
 * by Spring Boot's auto-configured DataSource. This class can be gradually removed once all
 * usages are migrated to Spring-managed beans.
 */
@Component
public class DatabaseConnectionManager {

    private final DataSource dataSource;

    public DatabaseConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
