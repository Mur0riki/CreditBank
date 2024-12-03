package neoflex.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neoflex.calculator.dto.CreditDto;
import neoflex.calculator.dto.LoanOfferDto;
import neoflex.calculator.dto.LoanStatementRequestDto;
import neoflex.calculator.dto.ScoringDataDto;
import neoflex.calculator.service.CalculatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(name = "CalculatorController", description = "Кредитный калькулятор.")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @PostMapping("/offers")
    @Operation(summary = "Получение списка кредитных предложений", description = "Создаётся 4 кредитных предложения LoanOfferDto" +
            "на основании четырёх возможных комбинаций isSalaryClient и isInsuranceEnabled")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDTO) {
        return new ResponseEntity<>(calculatorService.getLoanOffers(loanStatementRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/calc")
    @Operation(summary = "Расчет кредита", description = "Происходит скоринг данных, высчитывание итоговой ставки(rate), " +
            "полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), " +
            "график ежемесячных платежей (List<PaymentScheduleElementDto>)")
    public ResponseEntity<CreditDto> calculateCreditConditions(@RequestBody @Valid ScoringDataDto scoringDataDTO) {
        return new ResponseEntity<>(calculatorService.calculateCreditConditions(scoringDataDTO), HttpStatus.OK);
    }
}
