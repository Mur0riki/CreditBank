package neoflex.calculator.service;

import neoflex.calculator.dto.CreditDto;
import neoflex.calculator.dto.LoanOfferDto;
import neoflex.calculator.dto.LoanStatementRequestDto;
import neoflex.calculator.dto.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDTO);

    CreditDto calculateCreditConditions(ScoringDataDto scoringDataDTO);
}
