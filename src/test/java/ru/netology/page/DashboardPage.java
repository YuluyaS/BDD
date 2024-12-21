package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    //private static final ElementsCollection $$ =? ;
    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement heading = $ ("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$ (".list__item div");
    private final SelenideElement reloadButton = $ ("[data-test-id='action-reload']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getFirstCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId())).getText();
        return extractBalance(text);
    }

    //public int getFirstCardBalance(int index) {
        //var text = cards.get(index).getText();
        //return extractBalance(text);

    //}

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId())).$("button").click();
        return new TransferPage();
    }

    public void reloadDashboardPage(){
        reloadButton.click();
        heading.shouldBe(visible);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {

        int cardBalance;
        String testId = cardInfo.getTestId();

        var text = cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId())).getText();
        cardBalance = extractBalance(text);

        return cardBalance;
    }
}
