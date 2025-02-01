package neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import neoflex.deal.services.MessageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MessageController {
    MessageService messageService;
    @PostMapping("/deal/document/{statementId}/send")
    @Operation(summary = "Запрос на отправку документов", description = "")
    public void sendDocuments(@PathVariable("statementId") UUID statementId) {
        messageService.send(statementId);
    }
    @PostMapping("/deal/document/{statementId}/sign")
    @Operation(summary = "Запрос на подписание документов документов", description = "")
    public void signDocuments(@PathVariable("statementId") UUID statementId) {

        messageService.sign(statementId);
    }
    @PostMapping("/deal/document/{statementId}/code")
    @Operation(summary = "Подписание документов", description = "")
    public void codeDocuments(@PathVariable("statementId") UUID statementId,String sesCode) {

        messageService.code(statementId,sesCode);
    }
}
