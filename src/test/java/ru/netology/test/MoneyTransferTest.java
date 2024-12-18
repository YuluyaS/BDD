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
        System.out.println("setup -> firstCardBalance=" + firstCardBalance);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        System.out.println("setup -> secondCardBalance=" + secondCardBalance);
    }

    @Test
    void shouldTransferFromFirstToSecond(){
        System.out.println("shouldTransferFromFirstToSecond -> firstCardBalance=" + firstCardBalance);
        System.out.println("shouldTransferFromFirstToSecond -> secondCardBalance=" + secondCardBalance);
        var amount = generateValidAmount(firstCardBalance);
        System.out.println("shouldTransferFromFirstToSecond -> amount=" + amount);
        var expectedBalanceFirstCard = firstCardBalance-amount;
        System.out.println("shouldTransferFromFirstToSecond -> expectedBalanceFirstCard=" + expectedBalanceFirstCard);
        var expectedBalanceSecondCard = secondCardBalance+amount;
        System.out.println("shouldTransferFromFirstToSecond -> expectedBalanceSecondCard=" + expectedBalanceSecondCard);
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        //assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        //assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

        assertAll(() -> assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard),
                 () -> assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard));

    }


    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        assertAll(() -> transferPage.findErrorMessage("Выполнена попытка перевода суммы превышающей остаток на карте"),
                  () -> dashboardPage.reloadDashboardPage(),
                  () -> assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                  () -> assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo)));
    }

}
