package neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.dto.FinishRegistrationRequestDto;
import neoflex.deal.dto.LoanOfferDto;
import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.dto.StatementDto;
import neoflex.deal.model.Statement;
import neoflex.deal.services.DealService;
import neoflex.deal.services.MessageService;
import neoflex.deal.services.StatementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;
    private final MessageService messageService;
    private final StatementService statementService;

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
        messageService.finishRegistration(loanOfferDto.getStatementId());
    }


    @PostMapping("/deal/calculate/{statementId}")
    @Operation(summary = "Расчёт заявки на кредит", description = "Отправляет запрос в калькулятор " +
            "на расчет кредита с данными клиента. " +
            "Создается сущность кредит, и заявка обновляется результатом.")
    public void calculate(
            @PathVariable("statementId")UUID statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        dealService.calculate(statementId, finishRegistrationRequestDto);
    }
    @GetMapping("/admin/statement/{statementId}")
    @Operation(summary = "получение заявки",
            description = """
                    Приходит statementId. Ответ - statement.""")
    public ResponseEntity<Statement> getStatementDto(@PathVariable("statementId") UUID statementId) {
        log.info("statementId - {}", statementId);
        Statement statementDto = statementService.findStatementById(statementId);
        log.info("statementDto - {}", statementDto);
        return ResponseEntity.ok(statementDto);
    }

    @GetMapping("/admin/statement")
    @Operation(summary = "получение всех заявок",
            description = """
                   Ответ - List<Statement>.""")
    public ResponseEntity<List<Statement>> getAllStatements() {
        List<Statement> statementList = statementService.findAllStatement();
        log.info("statementDtoList - {}", statementList);
        return ResponseEntity.ok(statementList);
    }
}
