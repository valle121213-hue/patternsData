package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity(Faker faker) {
        String[] cities = {
                "Москва",
                "Санкт-Петербург",
                "Пермь",
                "Новосибирск",
                "Екатеринбург",
                "Казань",
                "Нижний Новгород",
                "Сахалин",
                "Самара",
                "Омск"
        };
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(Faker faker) {
        String name = faker.name().lastName() + " " + faker.name().firstName();
        return name;
    }

    public static String generatePhone(Faker faker) {
        String phone = "+7" + faker.number().digits(10);
        return phone;
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            faker = new Faker(new Locale(locale));

            UserInfo user = new UserInfo(
                    generateCity(faker),
                    generateName(faker),
                    generatePhone(faker)
            );

            return user;
        }
    }

    @Value
    public static class UserInfo {
        private String city;
        private String name;
        private String phone;
    }
}
