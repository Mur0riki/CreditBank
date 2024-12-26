package neoflex.statement.client;

import neoflex.statement.dto.LoanOfferDto;
import neoflex.statement.dto.LoanStatementRequestDto;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealFacadeTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private DealFacade dealFacade;

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
    void shouldReturnLoanOffers() {
        // Given
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder().build();

        List<LoanOfferDto> loanOffers = Collections.nCopies(4, LoanOfferDto.builder().build());
        when(dealClient.getLoanOffers(loanStatementRequestDto)).thenReturn(loanOffers);

        // When
        List<LoanOfferDto> result = dealFacade.getLoanOffers(loanStatementRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        verify(dealClient, times(1)).getLoanOffers(loanStatementRequestDto);
    }

    @Test
    void shouldSelectLoanOffer() {
        // When
        dealFacade.selectOffer(loanOfferDto);

        // Then
        verify(dealClient, times(1)).selectOffer(loanOfferDto);
    }
}