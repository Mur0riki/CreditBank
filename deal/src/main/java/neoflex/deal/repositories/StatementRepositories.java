package neoflex.deal.repositories;

import java.util.UUID;

import neoflex.deal.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepositories extends JpaRepository<Statement, UUID> {}
