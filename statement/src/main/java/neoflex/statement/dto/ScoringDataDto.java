package neoflex.statement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import neoflex.statement.dto.enums.EmploymentStatus;
import neoflex.statement.dto.enums.Gender;
import neoflex.statement.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Данные для оценки")
@Builder
public class ScoringDataDto {

    @JsonProperty("amount")
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Сумма", example = "10000")
    private final BigDecimal amount;

    @JsonProperty("term")
    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Срок займа", example = "6")
    private final Integer term;

    @JsonProperty("first_name")
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя", example = "Ivanov")
    private final String firstName;

    @JsonProperty("last_name")
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия", example = "Petrov")
    private final String lastName;

    @JsonProperty("middle_name")
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество", example = "Petrovich")
    private final String middleName;

    @JsonProperty("gender")
    @Schema(description = "Пол", example = "MALE")
    private final Gender gender;

    @JsonProperty("birthdate")
    @Schema(description = "Дата рождения", example = "2000-01-10")
    private final LocalDate birthdate;

    @JsonProperty("passport_series")
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @JsonProperty("passport_number")
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;

    @JsonProperty("passport_issue_date")
    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private final LocalDate passportIssueDate;

    @JsonProperty("passport_issue_branch")
    @Schema(description = "Отделение выдачи паспорта", example = "220-220")
    private final String passportIssueBranch;

    @JsonProperty("marital_status")
    @Schema(description = "Семейное положение", example = "MARRIED")
    private final MaritalStatus maritalStatus;

    @JsonProperty("dependent_amount")
    @Schema(description = "Количество иждивенцев", example = "1")
    private final Integer dependentAmount;

    @JsonProperty("employment")
    @Schema(description = "Трудоустройство")
    private final EmploymentStatus employment;

    @JsonProperty("account_number")
    @Schema(description = "Номер банковского счёта")
    private final String accountNumber;

    @JsonProperty("is_insurance_enabled")
    @Schema(description = "Страховка", example = "true")
    private final Boolean isInsuranceEnabled;

    @JsonProperty("is_salary_client")
    @Schema(description = "Зарплатный клиент", example = "true")
    private final Boolean isSalaryClient;
}