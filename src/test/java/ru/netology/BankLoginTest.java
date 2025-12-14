package ru.netology;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {

    @AfterAll
    static void tearDown() {
        SQLHelper.cleanDatabase();
    }

    @Test
    void shouldLogin() {
        var authInfo = DataHelper.getAuthInfo();
        
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        
        var verificationCode = SQLHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        
        dashboardPage.verifyIsDashboardPage();
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        var invalidAuthInfo = DataHelper.getInvalidAuthInfo();
        
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        loginPage.login(invalidAuthInfo.getLogin(), invalidAuthInfo.getPassword());
        loginPage.verifyErrorNotification();
    }

    @Test
    void shouldBlockAfterThreeInvalidLogins() {
        var invalidAuthInfo = DataHelper.getInvalidAuthInfo();
        
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        
        // Три попытки неверного входа
        for (int i = 0; i < 3; i++) {
            loginPage.login(invalidAuthInfo.getLogin(), invalidAuthInfo.getPassword());
            if (i < 2) {
                loginPage.verifyErrorNotification();
                open("http://localhost:9999");
            }
        }
        
        // Проверяем, что пользователь заблокирован
        var status = SQLHelper.getUserStatus(invalidAuthInfo.getLogin());
        // Здесь должна быть проверка статуса blocked
    }
}
