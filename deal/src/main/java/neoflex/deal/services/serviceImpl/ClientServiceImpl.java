package neoflex.deal.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.mapping.ClientMapper;
import neoflex.deal.model.Client;
import neoflex.deal.repositories.ClientRepositories;
import neoflex.deal.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepositories clientRepositories;
    private final ClientMapper clientMapper;

    /**
      Создание клиента на основе LoanStatementRequestDto и сохранение в бд.
      @param loanStatementRequestDto объект {@link LoanStatementRequestDto}
      @return добавленный клиент {@link Client}
     */
    public Client createClient(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientMapper.toClient(loanStatementRequestDto);
        log.debug("save client {}", client);
        return clientRepositories.save(client);
    }
}
