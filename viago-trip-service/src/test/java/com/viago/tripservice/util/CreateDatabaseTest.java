package com.viago.tripservice.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabaseTest {

    @Test
    public void createDatabase() {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "mysql";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS trips";
            stmt.executeUpdate(sql);
            System.out.println("Database 'trips' created successfully or already exists.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create database", e);
        }
    }
}
