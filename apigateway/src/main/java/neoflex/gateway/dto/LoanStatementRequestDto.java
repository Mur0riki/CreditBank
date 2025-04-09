package neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Setter
@Getter
@Schema(description = "Заявка на получение кредита")
@Builder
@AllArgsConstructor
public class LoanStatementRequestDto {

    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Запрашиваемая сумма", example = "1000000")
    private final BigDecimal amount;

    @Min(value = 6, message = "Term cannot be less than 6")
    @Schema(description = "Срок займа в месяцах", example = "24")
    private final Integer term;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя заемщика", example = "Ivan")
    private final String firstName;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия заемщика", example = "Ivanov")
    private final String lastName;

    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество заемщика", example = "Ivanovich")
    private final String middleName;

    @Email(message = "Not valid email")
    @Schema(description = "Электронная почта", example = "example@gmail.com")
    private final String email;

    @Past(message = "Date cannot be future")
    @Schema(description = "Дата рождения", example = "2000-01-01")
    private final LocalDate birthdate;

    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;
}
