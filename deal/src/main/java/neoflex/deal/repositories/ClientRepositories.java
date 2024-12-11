package neoflex.deal.repositories;

import java.util.UUID;

import neoflex.deal.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepositories extends JpaRepository<Client, UUID> {}
