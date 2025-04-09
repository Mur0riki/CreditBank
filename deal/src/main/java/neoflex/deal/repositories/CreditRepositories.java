package neoflex.deal.repositories;

import java.util.UUID;

import neoflex.deal.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepositories extends JpaRepository<Credit, UUID> {}
