package services;

import jakarta.persistence.EntityNotFoundException;
import neoflex.deal.model.Client;
import neoflex.deal.model.Statement;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.repositories.StatementRepositories;
import neoflex.deal.services.StatementService;
import neoflex.deal.services.serviceImpl.StatementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @Mock
    private StatementRepositories statementRepositories;

    private StatementService statementService;

    private Client client;

    private Statement statement;

    private UUID statementId;

    @BeforeEach
    void setUp() {
        statementService = new StatementServiceImpl(statementRepositories);
        client =
                Client.builder().email("ivan.ivanov@example.com").firstName("Ivan").lastName("Ivanov").build();

        statement =
                Statement.builder().client(client).creationDate(java.time.LocalDateTime.now()).build();

        statementId = UUID.randomUUID();
    }

    @Test
    void testCreateStatement() {
        when(statementRepositories.save(any(Statement.class))).thenReturn(statement);

        Statement createdStatement = statementService.createStatement(client);

        ArgumentCaptor<Statement> captor = ArgumentCaptor.forClass(Statement.class);
        verify(statementRepositories).save(captor.capture());

        Statement capturedStatement = captor.getValue();
        assertEquals(Status.PREAPPROVAL, capturedStatement.getStatus());
        assertEquals(client, capturedStatement.getClient());
    }

    @Test
    void testSaveStatement() {
        when(statementRepositories.save(statement)).thenReturn(statement);

        Statement savedStatement = statementService.saveStatement(statement);

        assertNotNull(savedStatement);
        assertEquals(statement, savedStatement);

        verify(statementRepositories).save(statement);
    }

    @Test
    void testFindStatementById() {
        when(statementRepositories.findById(statementId)).thenReturn(Optional.of(statement));

        Statement foundStatement = statementService.findStatementById(statementId);

        assertNotNull(foundStatement);
        assertEquals(statement, foundStatement);

        verify(statementRepositories).findById(statementId);
    }

    @Test
    void testFindStatementById_NotFound() {
        when(statementRepositories.findById(statementId)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class, () -> statementService.findStatementById(statementId));

        verify(statementRepositories).findById(statementId);
    }
}
