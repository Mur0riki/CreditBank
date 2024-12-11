package neoflex.deal.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Семейное положение")
public enum MaritalStatus {
    @Schema(description = "Замужем/женат")
    MARRIED,

    @Schema(description = "В разводе")
    DIVORCED,
    @Schema(description = "Не в отношениях")
    SINGLE,
    @Schema(description = "Вдова/Вдовец")
    WIDOW_WIDOWER
}
