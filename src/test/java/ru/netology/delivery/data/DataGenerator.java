package ru.netology.delivery.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String result = date.format(formatter);
        System.out.println("Сгенерирована дата: " + result + " (сдвиг: " + shift + " дней)");
        return result;
    }

    public static String generateCity() {
        String[] cities = {"Москва", "Санкт-Петербург", "Казань", "Екатеринбург",
                "Новосибирск", "Краснодар"};
        String city = cities[random.nextInt(cities.length)];
        System.out.println("Сгенерирован город: " + city);
        return city;
    }

    public static String generateName() {
        String[] firstNames = {"Александр", "Дмитрий", "Михаил", "Сергей", "Андрей"};
        String[] lastNames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов"};
        String name = lastNames[random.nextInt(lastNames.length)] + " " + firstNames[random.nextInt(firstNames.length)];
        System.out.println("Сгенерировано имя: " + name);
        return name;
    }

    public static String generatePhone() {
        StringBuilder phone = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            phone.append(random.nextInt(10));
        }
        String phoneStr = phone.toString();
        System.out.println("Сгенерирован телефон: " + phoneStr);
        return phoneStr;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser() {
            String city = generateCity();
            String name = generateName();
            String phone = generatePhone();
            return new UserInfo(city, name, phone);
        }
    }

    public static class UserInfo {
        private final String city;
        private final String name;
        private final String phone;

        public UserInfo(String city, String name, String phone) {
            this.city = city;
            this.name = name;
            this.phone = phone;
        }

        public String getCity() { return city; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
    }
}