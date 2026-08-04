package ru.netology.delivery;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generatePhone() {
        return "+7" + faker.number().digits(10);
    }

    public static String generateDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
