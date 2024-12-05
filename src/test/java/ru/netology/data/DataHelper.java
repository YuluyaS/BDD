package ru.netology.data;


import ru.netology.page.VerificationPage;

public class DataHelper {
    private DataHelper() {
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001", "92df3flc-a033-48e6-8390-206f6blf56c0");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }

    public static int generateValidAmount(int balance) {
        return Math.abs(balance) / 10;
    }

    public static int generateInvalidAmount(int balance) {
        return Math.abs(balance) + 1;

    }

    //@Value
    public static class VerificationCode {
        String code;
    }


    //@Value
    public static class CardInfo {
        String cardNumber;
        String testId;
    }

    //@Value
    public static class AuthInfo {
        String login;
        String password;
    }

}




