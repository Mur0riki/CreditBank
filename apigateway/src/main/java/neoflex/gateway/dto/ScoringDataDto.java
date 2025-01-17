package neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import neoflex.gateway.model.enumFields.EmploymentStatus;
import neoflex.gateway.model.enumFields.Gender;
import neoflex.gateway.model.enumFields.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Данные для оценки")
@Builder
public class ScoringDataDto {

    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Сумма", example = "10000")
    private final BigDecimal amount;

    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Срок займа", example = "6")
    private final Integer term;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя", example = "Ivanov")
    private final String firstName;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия", example = "Petrov")
    private final String lastName;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество", example = "Petrovich")
    private final String middleName;

    @Schema(description = "Пол", example = "MALE")
    private final Gender gender;

    @Schema(description = "Дата рождения", example = "2000-01-10")
    private final LocalDate birthdate;

    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private final LocalDate passportIssueDate;

    @Schema(description = "Отделение выдачи паспорта", example = "220-220")
    private final String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private final MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    private final Integer dependentAmount;

    @Schema(description = "Трудоустройство")
    private final EmploymentStatus employment;

    @Schema(description = "Номер банковского счёта")
    private final String accountNumber;

    @Schema(description = "Страховка", example = "true")
    private final Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private final Boolean isSalaryClient;
}