package neoflex.deal.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.model.Credit;
import neoflex.deal.repositories.CreditRepositories;
import neoflex.deal.services.CreditService;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepositories creditRepositories;

    /**
     Сохранение кредита в бд
     @param credit {@link Credit}
     @return сохранённый кредит {@link Credit}
     */
    public Credit saveCredit(Credit credit) {
        log.debug("save credit {}", credit);
        return creditRepositories.save(credit);
    }
}
