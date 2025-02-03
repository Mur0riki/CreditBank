# Используем образ Maven с OpenJDK 11
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY ./pom.xml /app

# Копируем pom.xml модуля deal
COPY deal/pom.xml /app/deal/

# Копируем исходный код модуля deal
COPY deal/src /app/deal/src/

# Собираем проект
RUN mvn clean package -f deal/pom.xml

# Используем образ для выполнения приложения
FROM eclipse-temurin:21 as runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=builder /app/deal/target/deal-1.0-SNAPSHOT.jar ./deal.jar

EXPOSE 8081
# Указываем команду для запуска приложения
CMD ["java", "-jar", "deal.jar"]