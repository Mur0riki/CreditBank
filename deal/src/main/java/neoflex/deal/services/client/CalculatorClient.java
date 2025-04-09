package neoflex.deal.services.client;

import neoflex.deal.dto.CreditDto;
import neoflex.deal.dto.LoanOfferDto;
import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.dto.ScoringDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;



@FeignClient(value = "calculator-mc" ,url = "http://localhost:8080")
public interface CalculatorClient {
    @PostMapping("/offers")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping("/calс")
    CreditDto getCredit(ScoringDataDto scoringDataDto);
}