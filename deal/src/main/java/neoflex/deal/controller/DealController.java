package neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.dto.FinishRegistrationRequestDto;
import neoflex.deal.dto.LoanOfferDto;
import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.services.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @PostMapping("/deal/statement")
    @Operation(summary = "Получение списка кредитных предложений", description = "Создаётся 4 кредитных предложения " +
            "LoanOfferDto " +
            "на основании четырёх возможных комбинаций isSalaryClient и isInsuranceEnabled")
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return dealService.createStatement(loanStatementRequestDto);
    }

    @PostMapping("/deal/offer/select")
    @Operation(summary = "Выбор кредитного предложения", description = "Обновляет статус заявки, " +
            "историю статусов и выбранное кредитное предложение.")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        dealService.selectOffer(loanOfferDto);
    }


    @PostMapping("/deal/calculate/{statementId}")
    @Operation(summary = "Расчёт заявки на кредит", description = "Отправляет запрос в калькулятор " +
            "на расчет кредита с данными клиента. " +
            "Создается сущность кредит, и заявка обновляется результатом.")
    public void calculate(
            @PathVariable("statementId")UUID statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        dealService.calculate(statementId, finishRegistrationRequestDto);
    }
}
