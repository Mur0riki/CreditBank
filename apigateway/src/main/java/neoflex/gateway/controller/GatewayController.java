package neoflex.gateway.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neoflex.gateway.client.CalculatorClient;
import neoflex.gateway.client.DealClient;
import neoflex.gateway.client.StatementClient;
import neoflex.gateway.dto.*;
import neoflex.gateway.model.Statement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
@Tag(name = "GatewayController")
public class GatewayController {
    StatementClient statementClient;
    DealClient dealClient;
    CalculatorClient calculatorClient;
    @PostMapping("/statement")
    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto){
        return statementClient.calculateLoanOffers(loanStatementRequestDto);
    }
    @PostMapping("/offer")
    void selectOfferStatement(LoanOfferDto loanOfferDto){
        statementClient.selectOffer(loanOfferDto);
    }
    @PostMapping("/deal/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto loanStatementRequestDto){
        return dealClient.createStatement(loanStatementRequestDto);
    }

    @PostMapping("/deal/offer/select")
    void selectOffer(LoanOfferDto loanOfferDto){
        dealClient.selectOffer(loanOfferDto);
    }

    @PostMapping("/deal/calculate/{statementId}")
    void calculate(@PathVariable("statementId") UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto){
        dealClient.calculate(statementId,finishRegistrationRequestDto);
    }

    @GetMapping("/admin/statement/{statementId}")
    ResponseEntity<Statement> getStatementDto(@PathVariable("statementId") UUID statementId){
        return dealClient.getStatementDto(statementId);
    }

    @GetMapping("/admin/statement")
    ResponseEntity<List<Statement>> getAllStatements(){
        return dealClient.getAllStatements();
    }

    @PostMapping("/deal/document/{statementId}/send")
    void sendDocuments(@PathVariable("statementId") UUID statementId){
        dealClient.sendDocuments(statementId);
    }
    @PostMapping("/deal/document/{statementId}/sign")
    void signDocuments(@PathVariable("statementId") UUID statementId){
        dealClient.signDocuments(statementId);
    }
    @PostMapping("/deal/document/{statementId}/code")
    void codeDocuments(@PathVariable("statementId") UUID statementId){
        dealClient.codeDocuments(statementId);
    }
    @PostMapping("/offers")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto){
        return calculatorClient.getLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("/calс")
    CreditDto getCredit(ScoringDataDto scoringDataDto){
        return calculatorClient.getCredit(scoringDataDto);
    }
    @PostMapping("/document/{statementId}/sign/code")
    void verifySesCode(@PathVariable("statementId") UUID statementId, String sesCode){
        dealClient.verifySesCode(statementId,sesCode);
    }

}
