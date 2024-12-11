package neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("statement_id")
    @Schema(description = "ID кредитного предложения", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID statementId;

    @JsonProperty("requested_amount")
    @Schema(description = "Запрашиваемая сумма", example = "1000000")
    private BigDecimal requestedAmount;

    @JsonProperty("total_amount")
    @Schema(description = "Итоговая сумма", example = "1500000")
    private BigDecimal totalAmount;

    @JsonProperty("term")
    @Schema(description = "Срок займа в месяцах", example = "10")
    private Integer term;

    @JsonProperty("monthly_payment")
    @Schema(description = "Ежемесячный платеж", example = "15000")
    private BigDecimal monthlyPayment;

    @JsonProperty("rate")
    @Schema(description = "Процент кредита", example = "15")
    private BigDecimal rate;

    @JsonProperty("is_insurance_enabled")
    @Schema(description = "Страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @JsonProperty("is_salary_client")
    @Schema(description = "Зарплатный клиент", example = "true")
    private Boolean isSalaryClient;
}
