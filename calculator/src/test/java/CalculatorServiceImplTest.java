import neoflex.calculator.dto.*;
import neoflex.calculator.dto.enumDto.EmploymentStatus;
import neoflex.calculator.dto.enumDto.Gender;
import neoflex.calculator.dto.enumDto.MaritalStatus;
import neoflex.calculator.dto.enumDto.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import neoflex.calculator.service.CalculatorServiceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class CalculatorServiceImplTest {

    @Mock
    CalculatorServiceImpl calculatorService = new CalculatorServiceImpl();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(calculatorService, "baseRate", 10);
    }

    @Test
    void calculateCreditConditionsTest() {
        int expectedRate = 5;

        EmploymentDto employmentDTOTest = EmploymentDto.builder().employmentStatus(EmploymentStatus.DIRECTOR)
                .employerINN("inn")
                .salary(BigDecimal.valueOf(15000))
                .position(Position.MANAGER)
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();

        ScoringDataDto scoringDataMock = mock(ScoringDataDto.class);
        when(scoringDataMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        when(scoringDataMock.getTerm()).thenReturn(24);
        when(scoringDataMock.getGender()).thenReturn(Gender.MALE);
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996 - 1 - 17));
        when(scoringDataMock.getEmployment()).thenReturn(employmentDTOTest);
        when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(true);
        when(scoringDataMock.getIsSalaryClient()).thenReturn(false);
        when(scoringDataMock.getDependentAmount()).thenReturn(1);
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.MARRIED);

        CreditDto actualCreditDTO = calculatorService.calculateCreditConditions(scoringDataMock);

        assertEquals(expectedRate, actualCreditDTO.getRate().intValue());
    }

    @Test
    void getLoanOffersTest() {
        LoanStatementRequestDto loanAppMock = mock(LoanStatementRequestDto.class);
        when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        when(loanAppMock.getTerm()).thenReturn(60);
        when(loanAppMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996 - 01 - 17));

        LoanOfferDto loanOfferDTO1 = LoanOfferDto.builder().statementId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(132000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2200.00).setScale(2))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        LoanOfferDto loanOfferDTO2 = LoanOfferDto.builder().statementId(2L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(151800))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2530.00).setScale(2))
                .rate(BigDecimal.valueOf(10))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDto loanOfferDTO3 = LoanOfferDto.builder().statementId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(155200))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2586.67))
                .rate(BigDecimal.valueOf(11))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDto loanOfferDTO4 = LoanOfferDto.builder().statementId(4L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(175000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2916.67))
                .rate(BigDecimal.valueOf(15))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        List<LoanOfferDto> expectedLoanOffers = Stream.of(
                        loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4
                )
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .collect(Collectors.toList());

        List<LoanOfferDto> actualLoanOffers = calculatorService.getLoanOffers(loanAppMock);

        assertEquals(expectedLoanOffers, actualLoanOffers);
    }
}
