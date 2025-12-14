package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/app", "app", "pass"
        );
    }

    @SneakyThrows
    public static String getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return runner.query(conn, codeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConn()) {
            runner.execute(conn, "DELETE FROM auth_codes");
            runner.execute(conn, "DELETE FROM card_transactions");
            runner.execute(conn, "DELETE FROM cards");
            runner.execute(conn, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static String getUserStatus(String login) {
        var statusSQL = "SELECT status FROM users WHERE login = ?";
        try (var conn = getConn()) {
            return runner.query(conn, statusSQL, new ScalarHandler<>(), login);
        }
    }
}
