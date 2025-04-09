# Используем образ Maven с OpenJDK 11
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY ./pom.xml /app

# Копируем pom.xml модуля calculator
COPY calculator/pom.xml /app/calculator/

# Копируем исходный код модуля calculator
COPY calculator/src /app/calculator/src/

# Собираем проект
RUN mvn clean package -f calculator/pom.xml

# Используем образ для выполнения приложения
FROM eclipse-temurin:21 as runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=builder /app/calculator/target/calculator-1.0-SNAPSHOT.jar ./calculator.jar

EXPOSE 8080
# Указываем команду для запуска приложения
CMD ["java", "-jar", "calculator.jar"]