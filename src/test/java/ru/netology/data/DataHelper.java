package ru.netology.data;

import lombok.Getter;
import lombok.Value;
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
        return new CardInfo("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0");

                                                                     //92df3f1c-a033-48e6-8390-206f6b1f56c0


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


    @Value
    public static class VerificationCode {
        String code;

        public VerificationCode(String code) {

            this.code = code;
        }

    }


    @Value
    public static class CardInfo {
        String cardNumber;
        String testId;

        public CardInfo(String cardNumber, String testId) {

            this.cardNumber = cardNumber;
            this.testId = testId;
        }

        public String getCardNumber() {
            return this.cardNumber;
        }

        public String getTestId() {

            return this.testId;
        }
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;

        public AuthInfo(String login, String password) {

            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return this.login;
        }

        public String getPassword() {
            return this.password;
        }

    }
}




