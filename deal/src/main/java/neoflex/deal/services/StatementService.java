package neoflex.deal.services;

import neoflex.deal.model.Client;
import neoflex.deal.model.Statement;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.UUID;

public interface StatementService {
    Statement createStatement(Client client);
    Statement saveStatement(Statement statement);
    Statement findStatementById(UUID id);
    List<Statement> findAllStatement();
}
