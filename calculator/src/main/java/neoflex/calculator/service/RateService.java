package neoflex.calculator.service;

import neoflex.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface RateService {
    public BigDecimal scoringRate(ScoringDataDto scoringDataDTO);

    BigDecimal calculateRateBySalaryClient(boolean isSalaryClient, BigDecimal rate);

    BigDecimal calculateRateByInsurance(boolean isInsuranceEnabled, BigDecimal rate);
}
