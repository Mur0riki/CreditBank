package neoflex.gateway.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип изменения")
public enum ChangeType {
    @Schema(description = "Автоматически")
    AUTOMATIC,
    @Schema(description = "В ручном режиме")
    MANUAL
}
