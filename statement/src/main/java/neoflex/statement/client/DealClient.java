package neoflex.statement.client;

import neoflex.statement.dto.LoanOfferDto;
import neoflex.statement.dto.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "deal-mc", url = "http://localhost:8081")
public interface DealClient {
    @PostMapping("/deal/statement")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping("/deal/offer/select")
    void selectOffer(LoanOfferDto loanOfferDto);
}
