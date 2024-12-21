package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.DashboardPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.generateInvalidAmount;
import static ru.netology.data.DataHelper.generateValidAmount;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup(){
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    }


    @Test
    void shouldTransferFromFirstToSecond(){

        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance-amount;
        var expectedSecondCardBalance = secondCardBalance+amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertAll(() -> assertEquals(expectedFirstCardBalance, actualBalanceFirstCard),
                 () -> assertEquals(expectedSecondCardBalance, actualBalanceSecondCard));

    }


    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {

        var amount = generateInvalidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance;
        var expectedSecondCardBalance = secondCardBalance;

        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);

        //dashboardPage = transferPage.makeInvalidTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        assertAll(() -> transferPage.findErrorMessage("Ошибка! "),
                  //() -> transferPage.cancelButton(),
                  () -> dashboardPage.reloadDashboardPage(),
                  () -> assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                  () -> assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(secondCardInfo))
        );
    }



    @Test
    void shouldGetIncorrectBalancesForTransferIfAmountMoreBalance() {

        var amount = generateInvalidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;

        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);

        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        assertAll(() -> dashboardPage.reloadDashboardPage(),
                () -> assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                () -> assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(secondCardInfo))
        );
    }



}
