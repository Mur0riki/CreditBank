package services;

import neoflex.deal.model.Credit;
import neoflex.deal.repositories.CreditRepositories;
import neoflex.deal.services.CreditService;
import neoflex.deal.services.serviceImpl.CreditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepositories creditRepositories;


    private CreditService creditService;

    private Credit credit;

    @BeforeEach
    void setUp() {
        creditService = new CreditServiceImpl(creditRepositories);
        credit = Credit.builder()
                .amount(new BigDecimal(100000))
                .rate(new BigDecimal("12.0"))
                .term(12)
                .build();
    }

    @Test
    void testSaveCredit() {
        when(creditRepositories.save(credit)).thenReturn(credit);

        Credit savedCredit = creditService.saveCredit(credit);

        assertNotNull(savedCredit);
        assertEquals(new BigDecimal(100000), savedCredit.getAmount());
        assertEquals(new BigDecimal("12.0"), savedCredit.getRate());
        assertEquals(12, savedCredit.getTerm());

        verify(creditRepositories).save(credit);
    }
}
