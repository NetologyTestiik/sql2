package ru.netology;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

public class BankLoginTest {

    @Test
    void shouldLogin() throws Exception {
        System.out.println("=== Starting test ===");
        
        // 1. Проверяем подключение к БД
        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass")) {
            System.out.println("✓ Connected to database");
            
            // 2. Проверяем таблицы
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("Database tables:");
            boolean hasTables = false;
            while (rs.next()) {
                System.out.println("  - " + rs.getString(1));
                hasTables = true;
            }
            
            if (!hasTables) {
                System.out.println("✗ No tables found!");
            }
            
            // 3. Проверяем пользователя vasya
            rs = stmt.executeQuery("SELECT * FROM users WHERE login = 'vasya'");
            if (rs.next()) {
                System.out.println("✓ Found user vasya");
                System.out.println("  ID: " + rs.getString("id"));
                System.out.println("  Password hash: " + rs.getString("password"));
                System.out.println("  Status: " + rs.getString("status"));
            } else {
                System.out.println("✗ User vasya not found!");
            }
            
            // 4. Проверяем auth_codes
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM auth_codes");
            if (rs.next()) {
                System.out.println("✓ Auth codes table has " + rs.getInt("count") + " rows");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Database error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        System.out.println("=== Test completed ===");
    }
}
