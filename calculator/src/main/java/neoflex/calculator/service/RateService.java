package neoflex.calculator.service;

import neoflex.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface RateService {
    BigDecimal scoringRate(ScoringDataDto scoringDataDTO);

    BigDecimal prescoringRate(boolean isSalaryClient, boolean isInsuranceEnabled);
}
