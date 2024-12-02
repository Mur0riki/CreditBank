package neoflex.calculator.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.calculator.dto.ScoringDataDto;
import neoflex.calculator.dto.enumDto.EmploymentStatus;
import neoflex.calculator.dto.enumDto.Gender;
import neoflex.calculator.dto.enumDto.MaritalStatus;
import neoflex.calculator.dto.enumDto.Position;
import neoflex.calculator.exceptionhandling.ScoringException;
import neoflex.calculator.service.RateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private static final BigDecimal MAX_NUMBER_OF_SALARIES = BigDecimal.valueOf(20);
    private static final int MIN_AGE = 20;
    private static final int MAX_AGE = 60;
    private static final int MIN_CURRENT_WORK_EXPERIENCE = 3;
    private static final int MIN_TOTAL_WORK_EXPERIENCE = 12;
    private BigDecimal baseRate = new BigDecimal(BigInteger.TEN);
    @Override
    public BigDecimal scoringRate(ScoringDataDto scoringDataDTO) {
        log.info("STARTED SCORING RATE");
        BigDecimal creditRate = baseRate;
        validateScoringData(scoringDataDTO);
        creditRate = calculateRateByInsurance(scoringDataDTO.getIsInsuranceEnabled(), creditRate);
        creditRate = calculateRateByEmployeeStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByEmploymentPosition(scoringDataDTO, creditRate);
        creditRate = calculateRateByMaritalStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByDependent(scoringDataDTO, creditRate);
        creditRate = calculateRateByGender(scoringDataDTO, creditRate);
        creditRate = calculateRateBySalaryClient(scoringDataDTO.getIsSalaryClient(), creditRate);
        log.info("FINISHED SCORING RATE");
        return creditRate;
    }
    private void validateScoringData(ScoringDataDto scoringDataDTO) {
        log.info("STARTED VALIDATE SCORING DATA");
        List<String> listExceptionInfo = new ArrayList<>();
        int age = getAge(scoringDataDTO);
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            listExceptionInfo.add("Unemployed status");
        }
        if (scoringDataDTO.getEmployment().getSalary().multiply(MAX_NUMBER_OF_SALARIES).intValue() < scoringDataDTO.getAmount().intValue()) {
            listExceptionInfo.add("Amount is too much");
        }
        if (age < MIN_AGE || age > MAX_AGE) {
            listExceptionInfo.add("Unsuitable age");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < MIN_CURRENT_WORK_EXPERIENCE) {
            listExceptionInfo.add("Current work experience very small");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < MIN_TOTAL_WORK_EXPERIENCE) {
            listExceptionInfo.add("Total work experience very small");
        }
        if (!listExceptionInfo.isEmpty()) {
            listExceptionInfo.add(0, "Refusal: ");
            log.error("FINISHED VALIDATE SCORING DATA UNSUCCESSFULLY");
            throw new ScoringException(listExceptionInfo.toString());
        } else {
            log.info("FINISHED VALIDATE SCORING DATA SUCCESSFULLY");
        }
    }
    public BigDecimal calculateRateBySalaryClient(boolean isSalaryClient, BigDecimal rate) {
        log.info("STARTED CALCULATE RATE BY SALARY CLIENT");
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }
        log.info("FINISHED CALCULATE RATE BY SALARY CLIENT");
        return rate;
    }

    private BigDecimal calculateRateByDependent(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE RATE BY DEPENDENT");
        if (scoringDataDTO.getDependentAmount() > 1) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("FINISHED CALCULATE RATE BY DEPENDENT");
        return creditRate;
    }

    private BigDecimal calculateRateByGender(ScoringDataDto scoringDataDto, BigDecimal creditRate) {
        log.info("STARTED CALCULATE RATE BY GENDER");
        int age = getAge(scoringDataDto);
        if (scoringDataDto.getGender() == Gender.FEMALE && age >= 35 && age < 60) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDto.getGender() == Gender.MALE && age >= 30 && age < 55) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDto.getGender() == Gender.NOT_BINARY) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE RATE BY GENDER");
        return creditRate;
    }

    private BigDecimal calculateRateByMaritalStatus(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE RATE BY MARITAL STATUS");
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.MARRIED) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("FINISHED CALCULATE RATE BY MARITAL STATUS");
        return creditRate;
    }

    private BigDecimal calculateRateByEmploymentPosition(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE RATE BY EMPLOYMENT POSITION");
        if (scoringDataDTO.getEmployment().getPosition() == Position.MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        }
        if (scoringDataDTO.getEmployment().getPosition() == Position.TOP_MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(4));
        }
        log.info("FINISHED CALCULATE RATE BY EMPLOYMENT POSITION");
        return creditRate;
    }

    private BigDecimal calculateRateByEmployeeStatus(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE RATE BY EMPLOYEE STATUS");
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.DIRECTOR) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE RATE BY EMPLOYEE STATUS");
        return creditRate;
    }
    public BigDecimal calculateRateByInsurance(boolean isInsuranceEnabled, BigDecimal rate) {
        log.info("STARTED CALCULATE RATE BY INSURANCE");
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE RATE BY INSURANCE");
        return rate;
    }
    private int getAge(ScoringDataDto scoringDataDTO) {
        log.info("STARTED COUNTING AGE");
        int years = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        log.info("FINISHED COUNTING AGE");
        return years;
    }
}
