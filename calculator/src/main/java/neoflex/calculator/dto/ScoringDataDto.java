package neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import neoflex.calculator.dto.enumDto.Gender;
import neoflex.calculator.dto.enumDto.MaritalStatus;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Данные для оценки")
public class ScoringDataDto {

    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Сумма", example = "10000")
    private final BigDecimal amount;

    @Min(value = 6, message = "Term cannot be less than 6")
    @NotNull
    @Schema(description = "Срок займа", example = "6")
    private final Integer term;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя", example = "Ivanov")
    private final String firstName;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия", example = "Petrov")
    private final String lastName;

    @Pattern(regexp = "[a-zA-Z]{2,}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество", example = "Petrovich")
    private final String middleName;

    @NotNull
    @Schema(description = "Пол", example = "MALE")
    private final Gender gender;

    @NotNull
    @Schema(description = "Дата рождения", example = "2000-01-10")
    private final LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;

    @NotNull
    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private final LocalDate passportIssueDate;

    @NotNull
    @Schema(description = "Отдел выдачи паспорта", example = "Krasnoyarsk")
    private final String passportIssueBranch;

    @NotNull
    @Schema(description = "Семейное положение", example = "MARRIED")
    private final MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Количество иждивенцев", example = "1")
    private final Integer dependentAmount;

    @NotNull
    @Schema(description = "Трудоустройство")
    private final EmploymentDto employment;

    @NotNull
    @Schema(description = "Учетная запись")
    private final String accountNumber;

    @NotNull
    @Schema(description = "Страховка", example = "true")
    private final Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Зарплатный клиент", example = "true")
    private final Boolean isSalaryClient;
}
