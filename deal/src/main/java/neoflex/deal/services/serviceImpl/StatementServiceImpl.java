package neoflex.deal.services.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.model.Client;
import neoflex.deal.model.Statement;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.repositories.StatementRepositories;
import neoflex.deal.services.StatementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementRepositories statementRepositories;

    /**
     * Создаёт новую заявку на основе переданного клиента. Устанавливает начальный статус заявки как
     * PREAPPROVAL и сохраняет её в базе данных.
     *
     * @param client объект клиента, связанный с заявкой {@link Client}.
     * @return созданная и сохранённая заявка {@link Statement}.
     */
    public Statement createStatement(Client client) {
        Statement statement =
                Statement.builder().client(client).creationDate(LocalDateTime.now()).build();
        statement.setStatus(Status.PREAPPROVAL);
        return saveStatement(statement);
    }

    /**
     * Сохраняет переданную заявку в базе данных.
     *
     * @param statement объект заявки {@link Statement}.
     * @return сохранённая заявка.
     */
    public Statement saveStatement(Statement statement) {
        log.debug("save statement: {}", statement);
        return statementRepositories.save(statement);
    }

    /**
     * Ищет заявку по идентификатору.
     *
     * @param id уникальный идентификатор заявки {@link UUID}.
     * @return найденная заявка {@link Statement}.
     * @throws EntityNotFoundException если заявка с указанным идентификатором не найдена.
     */
    public Statement findStatementById(UUID id) {
        log.debug("find statement by ID: {}", id);
        return statementRepositories
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заявка с ID " + id + " не найдена"));
    }

    /**
     * Ищет все заявки
     *
     * @return Список заявок {@link List<Statement>}
     */
    public List<Statement> findAllStatement() {
        return statementRepositories.findAll();
    }
}