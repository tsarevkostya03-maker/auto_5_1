# Delivery Test Automation

[![Java CI with Selenium](https://github.com/ВАШ-USERNAME/ВАШ-РЕПОЗИТОРИЙ/actions/workflows/main.yml/badge.svg)](https://github.com/ВАШ-USERNAME/ВАШ-РЕПОЗИТОРИЙ/actions/workflows/main.yml)

## Описание

Автотесты для формы заказа доставки карты.

## Запуск тестов локально

```bash
# Запустить SUT
java -jar artifacts/app-card-delivery.jar

# В другом терминале запустить тесты
./gradlew clean test