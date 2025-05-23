package neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import neoflex.calculator.dto.enumDto.EmploymentStatus;
import neoflex.calculator.dto.enumDto.Position;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Данные о трудовой занятости")
public class EmploymentDto {

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private final EmploymentStatus employmentStatus;

    @Schema(description = "ИНН", example = "123456")
    private final String employerINN;

    @Schema(description = "Оклад", example = "15000")
    private final BigDecimal salary;

    @Schema(description = "Должность", example = "MANAGER")
    private final Position position;

    @Schema(description = "Общий трудовой стаж", example = "12")
    private final Integer workExperienceTotal;

    @Schema(description = "Текущий трудовой стаж", example = "3")
    private final Integer workExperienceCurrent;
}
