package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {

    @Test
    void shouldLogin() throws SQLException {
        var usersSql = "SELECT login, password FROM users WHERE login = 'vasya';";
        var authCodesSql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";

        try (
            var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
            );
            var usersStmt = conn.createStatement();
            var authStmt = conn.createStatement();
        ) {
            var usersRs = usersStmt.executeQuery(usersSql);
            if (usersRs.next()) {
                var login = usersRs.getString("login");
                var password = usersRs.getString("password");
                System.out.println("Login: " + login + ", Password hash: " + password);
            }

            open("http://localhost:9999");
            $("[data-test-id=login] input").setValue("vasya");
            $("[data-test-id=password] input").setValue("qwerty123");
            $("[data-test-id=action-login]").click();

            var authRs = authStmt.executeQuery(authCodesSql);
            if (authRs.next()) {
                var code = authRs.getString("code");
                System.out.println("Code from DB: " + code);
                $("[data-test-id=code] input").setValue(code);
                $("[data-test-id=action-verify]").click();
            }

            $("h2").shouldHave(Condition.exactText("Личный кабинет"));
        }
    }
}
