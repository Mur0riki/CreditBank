package neoflex.statement.controllers;

import neoflex.statement.controller.StatementController;
import neoflex.statement.dto.LoanOfferDto;
import neoflex.statement.dto.LoanStatementRequestDto;
import neoflex.statement.service.StatementService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementController statementController;

    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(BigDecimal.class, () -> BigDecimal.valueOf(100000))
                .randomize(String.class, () -> "Ivan");

        EasyRandom easyRandom = new EasyRandom(parameters);
        loanStatementRequestDto = easyRandom.nextObject(LoanStatementRequestDto.class);
        loanOfferDto = easyRandom.nextObject(LoanOfferDto.class);
    }

    @Test
    void testCalculateLoanOffers_Success() {
        // When
        statementController.calculateLoanOffers(loanStatementRequestDto);

        // Then
        verify(statementService, times(1)).calculateLoanOffers(loanStatementRequestDto);
    }

    @Test
    void testSelectOffer_Success() {
        // When
        statementController.selectOffer(loanOfferDto);

        // Then
        verify(statementService, times(1)).selectOffer(loanOfferDto);
    }
}
