package neoflex.gateway.model.json;

import lombok.*;
import neoflex.gateway.model.enumFields.ChangeType;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {
    private String status;
    private LocalDate time;
    private ChangeType type;
}
