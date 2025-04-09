package neoflex.deal.services;

import neoflex.deal.dto.FinishRegistrationRequestDto;
import neoflex.deal.dto.LoanOfferDto;
import neoflex.deal.dto.LoanStatementRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DealService {
    ResponseEntity<List<LoanOfferDto>> createStatement(
            LoanStatementRequestDto loanStatementRequestDto);
    void selectOffer(LoanOfferDto loanOfferDto);
    void calculate(
            UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);

}
