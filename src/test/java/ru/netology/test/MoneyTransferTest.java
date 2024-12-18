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
        //System.out.println("setup -> firstCardBalance=" + firstCardBalance);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        //System.out.println("setup -> secondCardBalance=" + secondCardBalance);
    }

    /*
    @Test
    void shouldTransferFromFirstToSecond(){
        //System.out.println("shouldTransferFromFirstToSecond -> firstCardBalance=" + firstCardBalance);
        //System.out.println("shouldTransferFromFirstToSecond -> secondCardBalance=" + secondCardBalance);
        var amount = generateValidAmount(firstCardBalance);
        //System.out.println("shouldTransferFromFirstToSecond -> amount=" + amount);
        var expectedBalanceFirstCard = firstCardBalance-amount;
        //System.out.println("shouldTransferFromFirstToSecond -> expectedBalanceFirstCard=" + expectedBalanceFirstCard);
        var expectedBalanceSecondCard = secondCardBalance+amount;
        //System.out.println("shouldTransferFromFirstToSecond -> expectedBalanceSecondCard=" + expectedBalanceSecondCard);
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertAll(() -> assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard),
                 () -> assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard));

    }
    */


    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {
        System.out.println("shouldGetErrorMessageIfAmountMoreBalance -> secondCardBalance=" + secondCardBalance);
        var amount = generateInvalidAmount(secondCardBalance);
        System.out.println("shouldGetErrorMessageIfAmountMoreBalance -> amount=" + amount);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        System.out.println("shouldGetErrorMessageIfAmountMoreBalance -> expected=" + firstCardBalance);
        System.out.println("shouldGetErrorMessageIfAmountMoreBalance -> actual=" + dashboardPage.getCardBalance(firstCardInfo));

        assertAll(() -> transferPage.findErrorMessage("Ошибка! "),
                  () -> dashboardPage.reloadDashboardPage(),
                  () -> assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                  () -> assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo)));
    }

    //Правильно я понимаю, что тест shouldGetErrorMessageIfAmountMoreBalance должен выполняться таким образом,
    // что для обеих карт значения балансов начальные и конечные должны совпадать ?
    //Т.е. никаких арифметических действий не должно выполняться с ним в методе makeTransfer ?
    //Т.е. требуется соответствующая доработка (проверка, что amount больше баланса) метода makeTransfer ?

}
