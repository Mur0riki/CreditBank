package neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Заявка на получение кредита", name = "Заявка")
public class LoanStatementRequestDto {

    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Запрашиваемая сумма", example = "1000000")
    private final BigDecimal amount;

    @Min(value = 6, message = "Term cannot be less than 6")
    @NotNull
    @Schema(description = "Срок займа в месяцах", example = "24")
    private final Integer term;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя заемщика", example = "Ivan")
    private final String firstName;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия заемщика", example = "Ivanov")
    private final String lastName;

    @Pattern(regexp = "[a-zA-Z]{2,}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество заемщика", example = "Ivanovich")
    private final String middleName;

    @Email(message = "Not valid email")
    @Schema(description = "Электронная почта", example = "example@gmail.com")
    private final String email;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Дата рождения", example = "2000-01-01")
    private final LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;
}
