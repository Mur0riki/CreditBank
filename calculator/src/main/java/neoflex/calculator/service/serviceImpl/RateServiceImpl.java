package neoflex.calculator.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.calculator.dto.ScoringDataDto;
import neoflex.calculator.dto.enumDto.EmploymentStatus;
import neoflex.calculator.dto.enumDto.Gender;
import neoflex.calculator.dto.enumDto.MaritalStatus;
import neoflex.calculator.dto.enumDto.Position;
import neoflex.calculator.exceptionhandling.ScoringException;
import neoflex.calculator.service.RateService;
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
    private final BigDecimal baseRate = new BigDecimal(BigInteger.TEN);
    @Override
    public BigDecimal scoringRate(ScoringDataDto scoringDataDTO) {
        log.debug("Started scoring rate");
        BigDecimal creditRate = baseRate;
        validateScoringData(scoringDataDTO);
        creditRate = calculateRateByInsurance(scoringDataDTO.getIsInsuranceEnabled(), creditRate);
        creditRate = calculateRateByEmployeeStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByEmploymentPosition(scoringDataDTO, creditRate);
        creditRate = calculateRateByMaritalStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByDependent(scoringDataDTO, creditRate);
        creditRate = calculateRateByGender(scoringDataDTO, creditRate);
        creditRate = calculateRateBySalaryClient(scoringDataDTO.getIsSalaryClient(), creditRate);
        log.debug("Finished scoring rate");
        return creditRate;
    }
    @Override
    public BigDecimal prescoringRate(boolean isSalaryClient, boolean isInsuranceEnabled){
        log.debug("Started prescoring rate");
        BigDecimal rate = baseRate;
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(3));
        }
        log.debug("Finished prescoring rate");
        return rate;
    }
    private void validateScoringData(ScoringDataDto scoringDataDTO) {
        log.debug("Started validate scoring data");
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
            listExceptionInfo.addFirst("Refusal: ");
            log.error("Finished validate scoring dara unsuccessfully");
            throw new ScoringException(listExceptionInfo.toString());
        } else {
            log.debug("Finished validate scoring dara successfully");
        }
    }
    private BigDecimal calculateRateByDependent(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.debug("Started calculate rate by dependent");
        if (scoringDataDTO.getDependentAmount() > 1) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.debug("Finished calculate rate by dependent");
        return creditRate;
    }

    private BigDecimal calculateRateByGender(ScoringDataDto scoringDataDto, BigDecimal creditRate) {
        log.debug("Started calculate rate by gender");
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
        log.debug("Finished calculate rate by gender");
        return creditRate;
    }

    private BigDecimal calculateRateByMaritalStatus(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.debug("Started calculate rate by marital status");
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.MARRIED) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.debug("Finished calculate rate by marital status");
        return creditRate;
    }

    private BigDecimal calculateRateByEmploymentPosition(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.debug("Started calculate rate by employment position");
        if (scoringDataDTO.getEmployment().getPosition() == Position.MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        }
        if (scoringDataDTO.getEmployment().getPosition() == Position.TOP_MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(4));
        }
        log.debug("Finished calculate rate by employment position");
        return creditRate;
    }

    private BigDecimal calculateRateByEmployeeStatus(ScoringDataDto scoringDataDTO, BigDecimal creditRate) {
        log.debug("Started calculate rate by employee status");
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.DIRECTOR) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.debug("Finished calculate rate by employee status");
        return creditRate;
    }
    private int getAge(ScoringDataDto scoringDataDTO) {
        return Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
    }
    private BigDecimal calculateRateByInsurance(boolean isInsuranceEnabled, BigDecimal creditRate){
        if (isInsuranceEnabled) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        } else {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        return creditRate;
    }
    private BigDecimal calculateRateBySalaryClient(boolean isSalaryClient, BigDecimal creditRate){
        if (isSalaryClient) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        } else {
            creditRate = creditRate.add(BigDecimal.valueOf(2));
        }
        return creditRate;
    }
}
