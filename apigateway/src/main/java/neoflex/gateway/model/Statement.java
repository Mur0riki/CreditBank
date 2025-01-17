package neoflex.gateway.model;

import lombok.*;
import neoflex.gateway.dto.LoanOfferDto;
import neoflex.gateway.model.enumFields.Status;
import neoflex.gateway.model.json.StatusHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Statement {

    private UUID statementId;

    private Client client;

    private Credit credit;

    private Status status;

    private LocalDateTime creationDate;

    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    private List<StatusHistory> statusHistory;

}
