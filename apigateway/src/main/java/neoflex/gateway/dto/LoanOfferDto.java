package neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
@Schema(description = "Кредитное предложение")
@AllArgsConstructor
public class LoanOfferDto {

    @Schema(description = "ID кредитного предложения", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID statementId;

    @Schema(description = "Запрашиваемая сумма", example = "1000000")
    private BigDecimal requestedAmount;

    @Schema(description = "Итоговая сумма", example = "1500000")
    private BigDecimal totalAmount;

    @Schema(description = "Срок займа в месяцах", example = "10")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "15000")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процент кредита", example = "15")
    private BigDecimal rate;

    @Schema(description = "Страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private Boolean isSalaryClient;
}
