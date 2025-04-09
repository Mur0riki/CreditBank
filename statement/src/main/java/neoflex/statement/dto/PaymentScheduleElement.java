package neoflex.statement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Расчетный лист")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaymentScheduleElement {

    @JsonProperty("number")
    @Schema(description = "Номер платежа", example = "1")
    private final Integer number;

    @JsonProperty("date")
    @Schema(description = "Дата платежа", example = "2022-08-17")
    private final LocalDate date;

    @JsonProperty("total_payment")
    @Schema(description = "Всего к оплате ", example = "1500")
    private final BigDecimal totalPayment;

    @JsonProperty("interest_payment")
    @Schema(description = "Выплата процентов ", example = "1500")
    private final BigDecimal interestPayment;

    @JsonProperty("debt_payment")
    @Schema(description = "Выплата долга ", example = "2000")
    private final BigDecimal debtPayment;

    @JsonProperty("remaining_debt")
    @Schema(description = "Оставшийся долг ", example = "5000")
    private final BigDecimal remainingDebt;
}
