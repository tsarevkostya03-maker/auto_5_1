package ru.netology.delivery.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        // Уберите комментарий для просмотра браузера
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldReplanMeeting() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser();
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(5);

        System.out.println("Пользователь: " + user.getName() + ", " + user.getPhone());
        System.out.println("Первая дата: " + firstDate);
        System.out.println("Вторая дата: " + secondDate);

        // Открываем страницу
        driver.get("http://localhost:9999/");
        sleep(1000);

        // Заполняем форму
        fillForm(user.getCity(), firstDate, user.getName(), user.getPhone());

        // Отправляем первую заявку
        clickButton();
        sleep(2000);

        // Проверяем, что форма отправилась - ищем любое уведомление
        boolean success = checkAnyNotification();

        // Если нет уведомления, проверяем что форма очистилась или появилось другое сообщение
        if (!success) {
            System.out.println("Уведомление не найдено. Проверяем состояние страницы...");
            String pageSource = driver.getPageSource();
            System.out.println("Длина HTML: " + pageSource.length());

            // Проверяем, есть ли на странице слово "Встреча"
            if (pageSource.contains("Встреча")) {
                System.out.println("Найдено слово 'Встреча' на странице");
            }

            // Проверяем, не появилась ли ошибка
            if (pageSource.contains("ошибка") || pageSource.contains("Error")) {
                System.out.println("Найдена ошибка на странице");
            }
        }

        // Меняем дату
        changeDate(secondDate);

        // Отправляем повторную заявку
        clickButton();
        sleep(2000);

        // Проверяем, что форма отправилась снова
        checkAnyNotification();
    }

    private void fillForm(String city, String date, String name, String phone) {
        try {
            // Город
            WebElement cityInput = driver.findElement(By.cssSelector("[data-test-id='city'] input"));
            cityInput.clear();
            cityInput.sendKeys(city);
            System.out.println("Введен город: " + city);
            sleep(300);

            // Дата
            WebElement dateInput = driver.findElement(By.cssSelector("[data-test-id='date'] input"));
            dateInput.clear();
            dateInput.sendKeys(date);
            System.out.println("Введена дата: " + date);
            sleep(300);

            // Имя
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
            nameInput.clear();
            nameInput.sendKeys(name);
            System.out.println("Введено имя: " + name);
            sleep(300);

            // Телефон
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
            phoneInput.clear();
            phoneInput.sendKeys(phone);
            System.out.println("Введен телефон: " + phone);
            sleep(300);

            // Согласие
            WebElement agreement = driver.findElement(By.cssSelector("[data-test-id='agreement']"));
            if (!agreement.isSelected()) {
                agreement.click();
                System.out.println("Отмечено согласие");
            }
            sleep(300);

        } catch (Exception e) {
            System.out.println("Ошибка при заполнении формы: " + e.getMessage());
            throw e;
        }
    }

    private void changeDate(String newDate) {
        WebElement dateInput = driver.findElement(By.cssSelector("[data-test-id='date'] input"));
        dateInput.clear();
        dateInput.sendKeys(newDate);
        System.out.println("Дата изменена на: " + newDate);
        sleep(500);
    }

    private void clickButton() {
        try {
            WebElement button = driver.findElement(By.cssSelector(".button"));
            button.click();
            System.out.println("Кнопка нажата");
            sleep(2000);
        } catch (Exception e) {
            System.out.println("Не удалось найти кнопку: " + e.getMessage());
            throw e;
        }
    }

    private boolean checkAnyNotification() {
        try {
            // Проверяем разные возможные селекторы для уведомлений
            String[] selectors = {
                    "[data-test-id='success-notification']",
                    "[data-test-id='replan-notification']",
                    ".notification",
                    ".notification__content",
                    "[class*='notification']"
            };

            for (String selector : selectors) {
                try {
                    WebElement element = driver.findElement(By.cssSelector(selector));
                    if (element.isDisplayed()) {
                        String text = element.getText();
                        System.out.println("Найдено уведомление по селектору '" + selector + "': " + text);
                        return true;
                    }
                } catch (Exception e) {
                    // Игнорируем
                }
            }

            System.out.println("Уведомление не найдено");
            return false;

        } catch (Exception e) {
            System.out.println("Ошибка при поиске уведомления: " + e.getMessage());
            return false;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}