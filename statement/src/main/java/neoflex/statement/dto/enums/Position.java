package neoflex.statement.dto.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Должность")
public enum Position {
    @Schema(description = "Обычный работник")
    WORKER,
    @Schema(description = "Менеджер среднего звена")
    MID_MANAGER,
    @Schema(description = "Высший менджмент")
    TOP_MANAGER,
    @Schema(description = "Владелец бизнеса")
    OWNER
}
