package neoflex.deal.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Рабочий статус")
@AllArgsConstructor
public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    EMPLOYED,
    BUSINESS_OWNER
}
