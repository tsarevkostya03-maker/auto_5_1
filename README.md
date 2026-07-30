# Delivery Test Automation

[![Java CI with Gradle](https://github.com/tsarevkostya03-maker/auto_5_1/actions/workflows/main.yml/badge.svg)](https://github.com/tsarevkostya03-maker/auto_5_1/actions/workflows/main.yml)

## Описание

Автотесты для формы заказа доставки карты.

## Запуск тестов локально

```bash
# Запустить SUT
java -jar artifacts/app-card-delivery.jar

# В другом терминале запустить тесты
./gradlew clean test
