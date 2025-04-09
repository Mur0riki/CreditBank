package neoflex.deal.services.serviceImpl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.deal.dto.*;
import neoflex.deal.exceptionhandling.PrescoringException;
import neoflex.deal.exceptionhandling.ScoringCalculationException;
import neoflex.deal.mapping.CreditMapper;
import neoflex.deal.mapping.ScoringDataMapper;
import neoflex.deal.model.Client;
import neoflex.deal.model.Credit;
import neoflex.deal.model.Statement;
import neoflex.deal.model.enumFields.ChangeType;
import neoflex.deal.model.enumFields.CreditStatus;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.services.ClientService;
import neoflex.deal.services.CreditService;
import neoflex.deal.services.DealService;
import neoflex.deal.services.MessageService;
import neoflex.deal.services.client.CalculatorClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final ClientService clientService;
    private final StatementServiceImpl statementService;
    private final CreditService creditService;
    private final CalculatorClient calculatorClient;
    private final ScoringDataMapper scoringDataMapper;
    private final CreditMapper creditMapper;
    private final MessageService messageService;

    /**
     Создаёт заявку на основе предоставленных данных, взаимодействует с микросервисом Калькулятор
     для получения кредитных предложений.
     @param loanStatementRequestDto объект запроса с данными для создания заявки {@link LoanStatementRequestDto}.
     @return список кредитных предложений {@link LoanOfferDto}.
     @throws PrescoringException если при отправке запроса в микросервис Калькулятор возникает ошибка предскоринга.
     */
    public ResponseEntity<List<LoanOfferDto>> createStatement(
            LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientService.createClient(loanStatementRequestDto);

        Statement statement = statementService.createStatement(client);


        List<LoanOfferDto> offers;
        try {
            log.info("Запрос к микросервису Калькулятор для получения предложений по заявке" + statement.getStatementId());
            offers = calculatorClient.getLoanOffers(loanStatementRequestDto);
        } catch (FeignException e) {
            log.info("Ошибка возникла при запросе к мироксервису калькулятор.");
            if (e.status() == 400) {
                statement.setStatus(Status.CC_DENIED, ChangeType.AUTOMATIC);
                statementService.saveStatement(statement);
                throw new PrescoringException(e.getMessage());
            }
            throw e;
        }

        offers.forEach(oldOffer -> oldOffer.setStatementId(statement.getStatementId()));
        log.debug("Создан список предложений для заявки " + statement.getStatementId() + ": {}", offers);
        return ResponseEntity.ok(offers);
    }

    /**
     Обрабатывает выбор одного из предложений кредита.
     @param loanOfferDto выбранное предложение {@link LoanOfferDto}.
     */
    public void selectOffer(LoanOfferDto loanOfferDto) {
        Statement statement = statementService.findStatementById(loanOfferDto.getStatementId());
        statement.setStatus(Status.APPROVED);
        statement.setAppliedOffer(loanOfferDto);
        statementService.saveStatement(statement);
        messageService.finishRegistration(loanOfferDto.getStatementId());
    }

    /**
     Завершает процесс регистрации и выполняет полный расчёт кредита.
     @param statementId                  идентификатор заявки {@link UUID}.
     @param finishRegistrationRequestDto данные для завершения регистрации {@link FinishRegistrationRequestDto}.
     @throws ScoringCalculationException если при запросе к микросервису Калькулятор возникает ошибка расчёта.
     */
    public void calculate(
            UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        log.info("Начат процесс завершения регистрации заявки " + statementId);
        Statement statement = statementService.findStatementById(statementId);
        ScoringDataDto scoringDataDto =
                scoringDataMapper.toScoringDataDto(statement, finishRegistrationRequestDto);

        CreditDto creditDto;
        try {
            log.info("Запрос к микросервису Калькулятор для выполнения расчёта по заявке" + statementId);

            creditDto = calculatorClient.getCredit(scoringDataDto);
            statement.setStatus(Status.CC_APPROVED);
            statementService.saveStatement(statement);
        } catch (FeignException e) {
            log.info("Ошибка при запросе к микросервису Калькулятор по заявке " + statementId);
            if (e.status() == 500) {
                statement.setStatus(Status.CC_DENIED);
                statementService.saveStatement(statement);
                throw new ScoringCalculationException(e.getMessage());
            }
            throw e;
        }

        Credit credit = creditMapper.toCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditService.saveCredit(credit);
        messageService.send(statementId);
    }
}
