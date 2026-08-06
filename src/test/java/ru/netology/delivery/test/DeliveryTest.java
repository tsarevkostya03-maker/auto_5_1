package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setUp() {
        // Настройка таймаута
        Configuration.timeout = 15000; // 15 секунд
        // Открываем страницу с формой заявки
        open("http://localhost:9999/");
    }

    @Test
    void shouldSubmitValidDeliveryRequest() {
        // Генерируем тестовые данные
        String city = DataGenerator.generateCity();
        String name = DataGenerator.generateName();
        String phone = DataGenerator.generatePhone();
        String date = DataGenerator.generateDate(3); // Дата: сегодня + 3 дня

        // Заполняем форму
        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(date);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();

        // Отправляем форму
        $$("button").find(Condition.exactText("Забронировать")).click();

        // Проверяем успешное уведомление
        $("[data-test-id='success-notification']").shouldBe(Condition.visible);
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }
}
