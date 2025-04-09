# Используем образ Maven с OpenJDK 11
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY ./pom.xml /app

# Копируем pom.xml модуля apigateway
COPY apigateway/pom.xml /app/apigateway/

# Копируем исходный код модуля apigateway
COPY apigateway/src apigateway/src/

# Собираем проект
RUN mvn clean package -f apigateway/pom.xml

# Используем образ для выполнения приложения
FROM eclipse-temurin:21 as runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=builder /app/apigateway/target/apigateway-1.0-SNAPSHOT.jar ./apigateway.jar

EXPOSE 8086
# Указываем команду для запуска приложения
CMD ["java", "-jar", "apigateway.jar"]