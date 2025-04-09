package neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import neoflex.deal.model.enumFields.EmploymentStatus;
import neoflex.deal.model.enumFields.Position;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Данные о трудовой занятости")
public class EmploymentDto {

    @JsonProperty("employment_status")
    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private final EmploymentStatus employmentStatus;

    @JsonProperty("employer_inn")
    @Schema(description = "ИНН", example = "123456")
    private final String employerINN;

    @JsonProperty("salary")
    @Schema(description = "Оклад", example = "15000")
    private final BigDecimal salary;

    @JsonProperty("position")
    @Schema(description = "Должность", example = "MANAGER")
    private final Position position;

    @JsonProperty("work_experience_total")
    @Schema(description = "Общий трудовой стаж", example = "12")
    private final Integer workExperienceTotal;

    @JsonProperty("work_experience_current")
    @Schema(description = "Текущий трудовой стаж", example = "3")
    private final Integer workExperienceCurrent;
}
