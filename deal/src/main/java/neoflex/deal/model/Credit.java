package neoflex.deal.model;

import jakarta.persistence.*;
import lombok.*;
import neoflex.deal.dto.PaymentScheduleElement;
import neoflex.deal.model.enumFields.CreditStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID creditId;

    @Column(nullable = false, precision = 22, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer term;

    @Column(nullable = false, precision = 22, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(nullable = false, precision = 22, scale = 2)
    private BigDecimal rate;

    @Column(nullable = false, precision = 22, scale = 2)
    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule")
    private List<PaymentScheduleElement> paymentSchedule;

    @Column(nullable = false)
    private Boolean insuranceEnabled;

    @Column(nullable = false)
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditStatus creditStatus;
}
