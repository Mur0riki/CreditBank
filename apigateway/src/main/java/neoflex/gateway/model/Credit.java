package neoflex.gateway.model;

import lombok.*;
import neoflex.gateway.dto.PaymentScheduleElement;
import neoflex.gateway.model.enumFields.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Credit {

    private UUID creditId;
    
    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    private List<PaymentScheduleElement> paymentSchedule;

    private Boolean insuranceEnabled;

    private Boolean salaryClient;

    private CreditStatus creditStatus;
}
