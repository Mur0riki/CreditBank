package neoflex.calculator.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.calculator.dto.*;
import neoflex.calculator.exceptionhandling.ValidationException;
import neoflex.calculator.service.CalculatorService;
import neoflex.calculator.service.RateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private static final int ADULT = 18;
    private static final int QUANTITY_MONTHS = 12;
    private final RateService rateService = new RateServiceImpl();

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDTO) {
        log.info("Start created loan offers list");
        List<LoanOfferDto> listLoanOfferDTO = Stream.of(
                        getAvailableOfferDTO(UUID.randomUUID(), true, true, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), false, true, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), true, false, loanStatementRequestDTO),
                        getAvailableOfferDTO(UUID.randomUUID(), false, false, loanStatementRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .collect(Collectors.toList());
        log.info("Finished created list of loan offers\n {}", listLoanOfferDTO);
        return listLoanOfferDTO;
    }

    private LoanOfferDto getAvailableOfferDTO(UUID statementId, boolean isInsuranceEnabled, boolean isSalaryClient, LoanStatementRequestDto loanStatementRequestDTO) {
        log.debug("Start created loan offer");

        if (Period.between(loanStatementRequestDTO.getBirthdate(), LocalDate.now()).getYears() < ADULT) {
            throw new ValidationException("Age must be over 18");
        }
        BigDecimal requestAmount = loanStatementRequestDTO.getAmount();
        Integer term = loanStatementRequestDTO.getTerm();
        BigDecimal rate = rateService.prescoringRate(isSalaryClient, isInsuranceEnabled);
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN)).multiply(monthlyPercent).multiply(BigDecimal.valueOf(term));
        BigDecimal totalAmount = calculateTotalAmount(requestAmount, isInsuranceEnabled, creditCoast);
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term);
        LoanOfferDto loanOffer = LoanOfferDto.builder().
                statementId(statementId).
                requestedAmount(loanStatementRequestDTO.getAmount())
                .totalAmount(totalAmount.setScale(0,RoundingMode.HALF_EVEN))
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.debug("Finished created loan offer");
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
        log.debug("Started calculate credit condition\n");
        BigDecimal amount = scoringDataDTO.getAmount();
        BigDecimal rate = rateService.scoringRate(scoringDataDTO);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal psk = calculatePSK(scoringDataDTO, rate);
        BigDecimal monthlyPayment = calculateMonthlyPayment(psk, term);
        List<PaymentScheduleElement> paymentScheduleElements = getPaymentSchedule(amount, term, psk, monthlyPayment, rate);
        log.debug("Finished calculate credit condition\n");
        return new CreditDto(amount, term, monthlyPayment, rate, psk, scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(), paymentScheduleElements);
    }

    private List<PaymentScheduleElement> getPaymentSchedule(BigDecimal amount, Integer term, BigDecimal psk, BigDecimal monthlyPayment, BigDecimal rate) {
        log.debug("Started create list of payment schedule element");
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

        log.debug("Finished create list of payment schedule element");
        return listPaymentSchedule;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term) {
        log.debug("Started calculate monthly payment");
        BigDecimal monthlyPayment = totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
        log.debug("Finished calculate monthly payment");
        return monthlyPayment;
    }

    private BigDecimal calculatePSK(ScoringDataDto scoringDataDTO, BigDecimal rate) {
        log.debug("Started calculate psk");
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal requestAmount = scoringDataDTO.getAmount();
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN))
                .multiply(monthlyPercent).multiply(BigDecimal.valueOf(scoringDataDTO.getTerm()));
        BigDecimal psk = requestAmount.add(creditCoast);
        if (Boolean.TRUE.equals(scoringDataDTO.getIsInsuranceEnabled())) {
            BigDecimal insuranceCoast = requestAmount.multiply(INSURANCE_RATE);
            psk = psk.add(insuranceCoast);
        }
        log.debug("Finished calculate psk");
        return psk;
    }
}
