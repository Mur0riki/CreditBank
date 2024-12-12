package neoflex.deal.model.json;

import lombok.*;
import neoflex.deal.model.enumFields.EmploymentStatus;
import neoflex.deal.model.enumFields.Position;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employment {

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;

}
