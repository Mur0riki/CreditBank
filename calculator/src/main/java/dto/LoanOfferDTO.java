package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@Schema(description = "Кредитное предложение")
public class LoanOfferDTO {

    @Schema(description = "Номер кредитного предложения", example = "1")
    private final Long statementId;

    @Schema(description = "Запрашиваемая сумма", example = "1000000")
    private final BigDecimal requestedAmount;

    @Schema(description = "Итоговая сумма", example = "1500000")
    private final BigDecimal totalAmount;

    @Schema(description = "Срок займа в месяцах", example = "10")
    private final Integer term;

    @Schema(description = "Ежемесячный платеж", example = "15000")
    private final BigDecimal monthlyPayment;

    @Schema(description = "Процент кредита", example = "15")
    private final BigDecimal rate;

    @Schema(description = "Страховка", example = "true")
    private final boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private final boolean isSalaryClient;
}
