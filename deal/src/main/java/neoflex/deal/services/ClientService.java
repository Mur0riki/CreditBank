package neoflex.deal.services;

import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.model.Client;

public interface ClientService {
    Client createClient(LoanStatementRequestDto loanStatementRequestDto);
}
