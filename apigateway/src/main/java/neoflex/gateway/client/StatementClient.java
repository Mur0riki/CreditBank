package neoflex.gateway.client;

import neoflex.gateway.dto.LoanOfferDto;
import neoflex.gateway.dto.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "statement-ms",url = "http://localhost:8082")
public interface StatementClient {
    @PostMapping("/statement")
    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
    @PostMapping("/offer")
    void selectOffer(LoanOfferDto loanOfferDto);
}
