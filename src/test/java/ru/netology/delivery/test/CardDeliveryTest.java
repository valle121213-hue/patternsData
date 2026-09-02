package ru.netology.delivery.test;

import com.codeborne.selenide.Selectors;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import ru.netology.delivery.data.DataGenerator;

import java.io.ByteArrayInputStream;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.*;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;


public class CardDeliveryTest {

    @BeforeAll
    static void setup() {
        SelenideLogger.addListener(
                "AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @BeforeEach
    void openApp() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {

        var validUser = DataGenerator.Registration.generateUser("ru");

        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(7);


        $("[data-test-id=city] input").setValue(validUser.getCity());

        $("[data-test-id=date] input").press(SHIFT, HOME, BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());

        $("[data-test-id=agreement]").click();
        $(Selectors.byText("Запланировать")).click();

        $(Selectors.withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);

        Allure.addAttachment(
                "successful-plan",
                "image/png",
                new ByteArrayInputStream(screenshot(OutputType.BYTES)),
                "png"
        );

        $("[data-test-id=date] input").press(SHIFT, HOME, BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(Selectors.byText("Запланировать")).click();

        $("[data-test-id=replan-notification] .notification__content").shouldHave(
                        text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);

        $("[data-test-id=replan-notification] button").click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(
                        exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);

        Allure.addAttachment(
                "successful-replan",
                "image/png",
                new ByteArrayInputStream(screenshot(OutputType.BYTES)),
                "png"
        );

    }
}