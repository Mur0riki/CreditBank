package neoflex.calculator.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.calculator.dto.*;
import neoflex.calculator.exceptionhandling.ValidationException;
import neoflex.calculator.service.CalculatorService;
import neoflex.calculator.service.RateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {
    private static final BigDecimal INSURANCE_RATE = BigDecimal.valueOf(0.02);
    private static final BigDecimal PERCENT = BigDecimal.valueOf(100);
    private static final BigDecimal BASE_RATE = new BigDecimal(BigInteger.TEN);
    private static final int ADULT = 18;
    private static final int QUANTITY_MONTHS = 12;
    private final RateService rateService = new RateServiceImpl();
    @Value("${app.baseRate:10}")
    private static Integer baseRate;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDTO) {
        log.info("START CREATED LOAN OFFER");
        List<LoanOfferDto> listLoanOfferDTO = Stream.of(
                        getAvailableOfferDTO(UUID.randomUUID(), true, true, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), true, false, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), false, true, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), false, false, loanStatementRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .collect(Collectors.toList());
        log.info("FINISHED CREATED LIST OF LOAN OFFER\n {}", listLoanOfferDTO);
        return listLoanOfferDTO;
    }

    private LoanOfferDto getAvailableOfferDTO(UUID applicationId, boolean isInsuranceEnabled, boolean isSalaryClient, LoanStatementRequestDto loanStatementRequestDTO) {
        log.info("START CREATED LOAN OFFER");

        if (Period.between(loanStatementRequestDTO.getBirthdate(), LocalDate.now()).getYears() < ADULT) {
            throw new ValidationException("Age must be over 18");
        }
        BigDecimal rate = BASE_RATE;
        BigDecimal requestAmount = loanStatementRequestDTO.getAmount();
        Integer term = loanStatementRequestDTO.getTerm();
        rate = rateService.calculateRateBySalaryClient(isSalaryClient, rate);
        rate = rateService.calculateRateByInsurance(isInsuranceEnabled, rate);
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN)).multiply(monthlyPercent).multiply(BigDecimal.valueOf(term));
        BigDecimal totalAmount = calculateTotalAmount(requestAmount, isInsuranceEnabled, creditCoast);
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term);
        LoanOfferDto loanOffer = LoanOfferDto.builder().
                statementId(UUID.randomUUID()).
                requestedAmount(loanStatementRequestDTO.getAmount())
                .totalAmount(totalAmount.setScale(0))
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("FINISHED CREATED LOAN OFFER");
        return loanOffer;
    }

    private BigDecimal calculateTotalAmount(BigDecimal requestAmount, boolean isInsuranceEnabled, BigDecimal creditCoast) {
        BigDecimal totalAmount = requestAmount;
        if (isInsuranceEnabled) {
            BigDecimal insuranceCoast = requestAmount.multiply(INSURANCE_RATE);
            totalAmount = totalAmount.add(insuranceCoast);
        }
        totalAmount = totalAmount.add(creditCoast);
        return totalAmount;
    }

    @Override
    public CreditDto calculateCreditConditions(ScoringDataDto scoringDataDTO) {
        log.info("STARTED CALCULATE CREDIT CONDITION\n");
        BigDecimal amount = scoringDataDTO.getAmount();
        BigDecimal rate = rateService.scoringRate(scoringDataDTO);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal psk = calculatePSK(scoringDataDTO, rate);
        BigDecimal monthlyPayment = calculateMonthlyPayment(psk, term);
        List<PaymentScheduleElement> paymentScheduleElements = getPaymentSchedule(amount, term, psk, monthlyPayment, rate);
        log.info("FINISHED CALCULATE CREDIT CONDITION\n");
        return new CreditDto(amount, term, monthlyPayment, rate, psk, scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(), paymentScheduleElements);
    }

    private List<PaymentScheduleElement> getPaymentSchedule(BigDecimal amount, Integer term, BigDecimal psk, BigDecimal monthlyPayment, BigDecimal rate) {
        log.info("STARTED CREATE LIST OF  PAYMENT SCHEDULE ELEMENT");
        List<PaymentScheduleElement> listPaymentSchedule = new ArrayList<>();

        for (int i = 1; i < term + 1; i++) {
            LocalDate dayOfPayment = LocalDate.now().plusMonths(i);
            BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(i));
            BigDecimal interestPayment = (amount.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))).divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
            BigDecimal remainingDebt = psk.subtract(totalPayment);
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement(i, dayOfPayment, totalPayment, interestPayment, debtPayment, remainingDebt);
            listPaymentSchedule.add(paymentScheduleElement);
        }

        log.info("FINISHED CREATE LIST OF  PAYMENT SCHEDULE ELEMENT");
        return listPaymentSchedule;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term) {
        log.info("STARTED CALCULATE MONTHLY PAYMENT");
        BigDecimal monthlyPayment = totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
        log.info("FINISHED CALCULATE MONTHLY PAYMENT");
        return monthlyPayment;
    }

    private BigDecimal calculatePSK(ScoringDataDto scoringDataDTO, BigDecimal rate) {
        log.info("STARTED CALCULATE PSK");
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal requestAmount = scoringDataDTO.getAmount();
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN))
                .multiply(monthlyPercent).multiply(BigDecimal.valueOf(scoringDataDTO.getTerm()));
        BigDecimal psk = requestAmount.add(creditCoast);
        if (Boolean.TRUE.equals(scoringDataDTO.getIsInsuranceEnabled())) {
            BigDecimal insuranceCoast = requestAmount.multiply(INSURANCE_RATE);
            psk = psk.add(insuranceCoast);
        }
        log.info("FINISHED CALCULATE PSK");
        return psk;
    }
}
