package neoflex.deal.model.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neoflex.deal.model.enumFields.ChangeType;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {
    private String status;
    private LocalDate time;
    private ChangeType type;
}
