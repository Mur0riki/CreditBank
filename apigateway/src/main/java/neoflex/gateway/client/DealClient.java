package neoflex.gateway.client;

import neoflex.gateway.dto.FinishRegistrationRequestDto;
import neoflex.gateway.dto.LoanOfferDto;
import neoflex.gateway.dto.LoanStatementRequestDto;
import neoflex.gateway.model.Statement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "deal-ms", url = "http://localhost:8081")
public interface DealClient {
    @PostMapping("/deal/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping("/deal/offer/select")
    void selectOffer(LoanOfferDto loanOfferDto);

    @PostMapping("/deal/calculate/{statementId}")
    void calculate(@PathVariable("statementId") UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);

    @GetMapping("/admin/statement/{statementId}")
    ResponseEntity<Statement> getStatementDto(@PathVariable("statementId") UUID statementId);

    @GetMapping("/admin/statement")
    ResponseEntity<List<Statement>> getAllStatements();

    @PostMapping("/deal/document/{statementId}/send")
    void sendDocuments(@PathVariable("statementId") UUID statementId);
    @PostMapping("/deal/document/{statementId}/sign")
    void signDocuments(@PathVariable("statementId") UUID statementId);
    @PostMapping("/deal/document/{statementId}/code")
    void codeDocuments(@PathVariable("statementId") UUID statementId);
}
