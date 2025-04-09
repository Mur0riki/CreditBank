package neoflex.gateway.client;

import neoflex.gateway.dto.CreditDto;
import neoflex.gateway.dto.LoanOfferDto;
import neoflex.gateway.dto.LoanStatementRequestDto;
import neoflex.gateway.dto.ScoringDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "calculator-ms", url = "http://localhost:8080")
public interface CalculatorClient {
    @PostMapping("/offers")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping("/calс")
    CreditDto getCredit(ScoringDataDto scoringDataDto);
}
