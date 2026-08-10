package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setUp() {
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
        $$("button").find(Condition.exactText("Запланировать")).click();

        // Проверяем успешное уведомление с явным ожиданием (15 секунд)
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + date), Duration.ofSeconds(15));
    }

    @Test
    void shouldReplanDelivery() {
        // 1. Планируем встречу на первоначальную дату
        String city = DataGenerator.generateCity();
        String name = DataGenerator.generateName();
        String phone = DataGenerator.generatePhone();
        String firstDate = DataGenerator.generateDate(3); // сегодня + 3 дня

        // Заполняем форму и отправляем
        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(firstDate);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Запланировать")).click();

        // Проверяем, что первое планирование успешно
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstDate), Duration.ofSeconds(15));

        // 2. Перепланируем встречу на новую дату
        String newDate = DataGenerator.generateDate(5); // сегодня + 5 дней

        // Открываем форму для перепланирования (на той же странице)
        // Очищаем поле даты и вводим новую
        $("[data-test-id='date'] input").doubleClick().sendKeys(newDate);
        $$("button").find(Condition.exactText("Запланировать")).click();

        // Проверяем, что перепланирование успешно (уведомление обновилось)
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + newDate), Duration.ofSeconds(15));
    }
}
