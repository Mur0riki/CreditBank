package neoflex.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Сущность кредита")
@Builder
public class CreditDto {

    @Schema(description = "Сумма", example = "1000000")
    private final BigDecimal amount;

    @Schema(description = "Срок займа", example = "24")
    private final Integer term;

    @Schema(description = "Ежемесячный платеж", example = "22000")
    private final BigDecimal monthlyPayment;

    @Schema(description = "Процент кредита", example = "22")
    private final BigDecimal rate;

    @Schema(description = "Полная стоимость кредита", example = "15")
    private final BigDecimal psk;

    @Schema(description = "Страховка", example = "false")
    private final Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private final Boolean isSalaryClient;

    @Schema(description = "График платежей")
    private final List<PaymentScheduleElement> paymentSchedule;
}
