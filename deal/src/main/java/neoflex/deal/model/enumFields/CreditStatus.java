package neoflex.deal.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Индикатор того в каком состоянии находится заявка")
public enum CreditStatus {
    @Schema(description = "Вычислена")
    CALCULATED,
    ISSUED
}
