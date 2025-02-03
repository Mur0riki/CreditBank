# Используем образ Maven с OpenJDK 11
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY ./pom.xml /app

# Копируем pom.xml модуля statement
COPY statement/pom.xml /app/statement/

# Копируем исходный код модуля statement
COPY statement/src /app/statement/src/

# Собираем проект
RUN mvn clean package -f statement/pom.xml -X

# Используем образ для выполнения приложения
FROM eclipse-temurin:21 as runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=builder /app/statement/target/statement-1.0-SNAPSHOT.jar ./statement.jar

EXPOSE 8082
# Указываем команду для запуска приложения
CMD ["java", "-jar", "statement.jar"]