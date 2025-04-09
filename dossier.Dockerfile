# Используем образ Maven с OpenJDK 11
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY pom.xml /app

# Копируем pom.xml модуля dossier
COPY dossier/pom.xml /app/dossier/

# Копируем исходный код модуля dossier
COPY dossier/src dossier/src/

# Собираем проект
RUN mvn clean package -f dossier/pom.xml -X

# Используем образ для выполнения приложения
FROM eclipse-temurin:21-jre as runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=builder /app/dossier/target/dossier-1.0-SNAPSHOT.jar ./dossier.jar

EXPOSE 8083
# Указываем команду для запуска приложения
CMD ["java", "-jar", "dossier.jar"]