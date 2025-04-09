package neoflex.deal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import neoflex.deal.dto.LoanOfferDto;
import neoflex.deal.model.enumFields.ChangeType;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.model.json.StatusHistory;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "statements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<StatusHistory> statusHistory;

    /**
     Устанавливает новый статус заявки с типом изменения по умолчанию {@link ChangeType#AUTOMATIC}.
     @param status новый статус заявки {@link Status}.
     */
    public void setStatus(Status status) {
        setStatus(status, ChangeType.AUTOMATIC);
    }

    /**
      Устанавливает новый статус заявки и добавляет запись в историю изменений статуса.
      @param status новый статус заявки {@link Status}.
      @param type тип изменения статуса (например, {@link ChangeType}).
     */
    public void setStatus(Status status, ChangeType type) {
        this.status = status;
        addStatusHistory(
                StatusHistory.builder().status(status.name()).time(LocalDate.now()).type(type).build());
    }

    /**
     Добавляет новую запись в историю изменений статусов заявки.
     @param status объект {@link StatusHistory}, содержащий данные о статусе, времени изменения и типе.
     */
    public void addStatusHistory(StatusHistory status) {
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        statusHistory.add(status);
    }
}
