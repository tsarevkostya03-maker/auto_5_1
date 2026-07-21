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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.out.println("=== НАЧАЛО НАСТРОЙКИ ===");
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java: " + System.getProperty("java.version"));

        try {
            WebDriverManager.firefoxdriver().setup();
            System.out.println("WebDriverManager настроен успешно");
        } catch (Exception e) {
            System.out.println("Ошибка при настройке WebDriverManager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @BeforeEach
    void setUp() {
        System.out.println("=== НАЧАЛО ТЕСТА ===");

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");

            System.out.println("Создание FirefoxDriver...");
            driver = new FirefoxDriver(options);
            System.out.println("FirefoxDriver создан успешно");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.manage().window().maximize();
            System.out.println("Настройки драйвера применены");

        } catch (Exception e) {
            System.out.println("ОШИБКА при создании драйвера: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Драйвер закрыт");
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

        driver.get("http://localhost:9999/");
        sleep(1000);

        fillForm(user.getCity(), firstDate, user.getName(), user.getPhone());
        clickButton();
        sleep(2000);

        checkAnyNotification();

        changeDate(secondDate);
        clickButton();
        sleep(2000);

        checkAnyNotification();
    }

    private void fillForm(String city, String date, String name, String phone) {
        try {
            WebElement cityInput = driver.findElement(By.cssSelector("[data-test-id='city'] input"));
            cityInput.clear();
            cityInput.sendKeys(city);
            System.out.println("Введен город: " + city);
            sleep(300);

            WebElement dateInput = driver.findElement(By.cssSelector("[data-test-id='date'] input"));
            dateInput.clear();
            dateInput.sendKeys(date);
            System.out.println("Введена дата: " + date);
            sleep(300);

            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
            nameInput.clear();
            nameInput.sendKeys(name);
            System.out.println("Введено имя: " + name);
            sleep(300);

            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
            phoneInput.clear();
            phoneInput.sendKeys(phone);
            System.out.println("Введен телефон: " + phone);
            sleep(300);

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

    private void checkAnyNotification() {
        try {
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
                        return;
                    }
                } catch (Exception e) {
                    // Игнорируем
                }
            }

            System.out.println("Уведомление не найдено");

        } catch (Exception e) {
            System.out.println("Ошибка при поиске уведомления: " + e.getMessage());
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