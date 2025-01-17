package neoflex.deal.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.dto.EmailMessageDTO;
import neoflex.deal.model.Credit;
import neoflex.deal.model.Statement;
import neoflex.deal.model.enumFields.CreditStatus;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.model.enumFields.Theme;
import neoflex.deal.model.json.StatusHistory;
import neoflex.deal.repositories.CreditRepositories;
import neoflex.deal.repositories.StatementRepositories;
import neoflex.deal.services.MessageService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StatementRepositories statementRepositories;
    private final CreditRepositories creditRepositories;

    @Override
    public void finishRegistration(UUID statementId) {
        Statement statement = getStatement(statementId);
        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(statement.getClient().getEmail())
                .statementId(statementId)
                .theme(Theme.FINISH_REGISTRATION)
                .build());
    }
    @Override
    public void send(UUID statementId) {
        Statement statement = getStatement(statementId);
        statement.setStatus(Status.PREPARE_DOCUMENTS);
        updateStatusHistory(statement, Status.PREPARE_DOCUMENTS);
        statementRepositories.save(statement);
        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(statement.getClient().getEmail())
                .statementId(statementId)
                .theme(Theme.CREATE_DOCUMENTS).build()
        );
    }

    @Override
    public void sign(UUID statementId) {
        Statement statement = getStatement(statementId);
        Integer sesCode = createSesCode();
        kafkaTemplate.send(getTopic(Theme.SEND_SES), sesCode.toString());

        log.debug("set sescode {} for statement {} ", sesCode, statement);
        statement.setSesCode(sesCode.toString());
        statement.setStatus(Status.DOCUMENT_SIGNED);
        log.debug("update statement in database");
        statementRepositories.save(statement);

        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(statement.getClient().getEmail())
                .statementId(statementId)
                .theme(Theme.SEND_DOCUMENTS)
                .build());
    }

    @Override
    public void code(UUID statementId) {
        log.debug("try get statement by id {} ", statementId);
        Statement statement = getStatement(statementId);
        statement.setStatus(Status.DOCUMENT_SIGNED);
        statement.setSignDate(LocalDateTime.now());
        updateStatusHistory(statement, Status.DOCUMENT_SIGNED);
        log.debug("update statement in database");
        statementRepositories.save(statement);

        issueCredit(statement);

        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(statement.getClient().getEmail())
                .statementId(statementId)
                .theme(Theme.CREDIT_ISSUED)
                .build()
        );
    }

    private void issueCredit(Statement statement) {
        if (statement.getCredit() == null) {
            log.info("\"credit " + statement.getStatementId() + " is not exists\"");
        }
        UUID creditId = statement.getCredit().getCreditId();
        Credit credit = creditRepositories.findById(creditId).get();

        statement.setStatus(Status.CREDIT_ISSUED);
        updateStatusHistory(statement, Status.CREDIT_ISSUED);
        credit.setCreditStatus(CreditStatus.ISSUED);
        creditRepositories.save(credit);
        log.info("update credit  in database");
    }

    private void updateStatusHistory(Statement statement, Status status) {
        statement.getStatusHistory().add(StatusHistory.builder()
                .time(LocalDate.now())
                .status(String.valueOf(status))
                .build()
        );
    }

    private Integer createSesCode() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private Statement getStatement(UUID statementId) {
        return statementRepositories.findById(statementId).get();
    }

    private void sendMessageForConsumer(EmailMessageDTO messageDTO) {
        String topic = getTopic(messageDTO.getTheme());
        kafkaTemplate.send(topic, messageDTO.toString());
    }

    private String getTopic(Theme theme) {
        String topic;

        switch (theme) {
            case FINISH_REGISTRATION:
                topic = "finish-registration";
                break;
            case CREATE_DOCUMENTS:
                topic = "create-documents";
                break;
            case SEND_DOCUMENTS:
                topic = "send-documents";
                break;
            case SEND_SES:
                topic = "send-ses";
                break;
            case CREDIT_ISSUED:
                topic = "credit-issued";
                break;
            case STATEMENT_DENIED:
                topic = "statement-denied";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + theme);
        }
        log.info("topic is definition {}", topic);
        return topic;
    }
}
