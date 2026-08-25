package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        // Добавляем слушатель Allure для автоматического логирования шагов и скриншотов
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)      // делать скриншоты при каждом шаге
                .savePageSource(true)); // сохранять HTML страницы при ошибках
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSubmitValidDeliveryRequest() {
        // Шаг Allure
        SelenideLogger.step("Генерация тестовых данных", () -> {
            String city = DataGenerator.generateCity();
            String name = DataGenerator.generateName();
            String phone = DataGenerator.generatePhone();
            String date = DataGenerator.generateDate(3);

            // Шаг Allure внутри шага
            SelenideLogger.step("Заполнение формы", () -> {
                $("[data-test-id='city'] input").setValue(city);
                $("[data-test-id='date'] input").doubleClick().sendKeys(date);
                $("[data-test-id='name'] input").setValue(name);
                $("[data-test-id='phone'] input").setValue(phone);
                $("[data-test-id='agreement']").click();
            });

            SelenideLogger.step("Отправка формы", () -> {
                $$("button").find(Condition.exactText("Запланировать")).click();
            });

            SelenideLogger.step("Проверка успешного уведомления", () -> {
                $("[data-test-id='success-notification']").shouldBe(Condition.visible);
                $("[data-test-id='success-notification'] .notification__content")
                        .shouldHave(Condition.exactText("Встреча успешно запланирована на " + date));
            });
        });
    }
}