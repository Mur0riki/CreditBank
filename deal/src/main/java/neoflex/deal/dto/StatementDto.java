package neoflex.deal.dto;

import lombok.*;
import neoflex.deal.model.Client;
import neoflex.deal.model.Credit;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.model.json.StatusHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StatementDto {

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