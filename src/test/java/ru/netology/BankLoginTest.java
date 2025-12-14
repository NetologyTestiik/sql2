package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {

    @BeforeAll
    static void setup() {
        System.out.println("Setting up tests...");
    }

    @Test
    void shouldLogin() throws SQLException {
        var usersSql = "SELECT login, password FROM users WHERE login = 'vasya';";
        var authCodesSql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";
        
        System.out.println("Starting test...");
        
        try (
            var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
            );
            var usersStmt = conn.createStatement();
            var authStmt = conn.createStatement();
        ) {
            System.out.println("Connected to database");
            
            // Проверяем таблицы
            var tables = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("Table: " + tables.getString("TABLE_NAME"));
            }
            
            var usersRs = usersStmt.executeQuery(usersSql);
            if (usersRs.next()) {
                var login = usersRs.getString("login");
                var password = usersRs.getString("password");
                System.out.println("Found user: " + login + ", Password hash: " + password);
            } else {
                System.out.println("User 'vasya' not found!");
            }

            open("http://localhost:9999");
            System.out.println("Opened browser");
            
            $("[data-test-id=login] input").setValue("vasya");
            $("[data-test-id=password] input").setValue("qwerty123");
            $("[data-test-id=action-login]").click();

            var authRs = authStmt.executeQuery(authCodesSql);
            if (authRs.next()) {
                var code = authRs.getString("code");
                System.out.println("Code from DB: " + code);
                $("[data-test-id=code] input").setValue(code);
                $("[data-test-id=action-verify]").click();
            } else {
                System.out.println("No auth code found in database!");
            }

            $("h2").shouldHave(Condition.exactText("Личный кабинет"));
            System.out.println("Test passed!");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
