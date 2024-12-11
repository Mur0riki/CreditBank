package neoflex.deal.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Должность")
public enum Position {
    WORKER,
    MID_MANAGER,
    TOP_MANAGER,
    OWNER
}
