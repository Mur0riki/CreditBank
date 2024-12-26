package neoflex.statement.dto.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рабочий статус")
public enum EmploymentStatus {
    @Schema(description = "Безработный")
    UNEMPLOYED,
    @Schema(description = "Самозанятый")
    SELF_EMPLOYED,
    @Schema(description = "Трудоустроен")
    EMPLOYED,
    @Schema(description = "ИП/ООО")
    BUSINESS_OWNER
}
