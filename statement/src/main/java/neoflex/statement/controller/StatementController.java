package neoflex.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import neoflex.statement.dto.LoanOfferDto;
import neoflex.statement.dto.LoanStatementRequestDto;
import neoflex.statement.service.StatementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    /**
     * Обрабатывает запрос на прескоринг и получение предложений по кредиту.
     *
     * <ul>
     *   <li>Получает {@link LoanStatementRequestDto} через API.
     *   <li>Выполняет прескоринг заявки на основе переданных данных.
     *   <li>Возвращает список из 4 предложений {@link LoanOfferDto}, упорядоченных от наименее
     *       выгодного к наиболее выгодному.
     * </ul>
     *
     * @param loanStatementRequestDto данные заявки на кредит
     * @return список кредитных предложений {@link LoanOfferDto}
     */
    @PostMapping("/statement")
    @Operation(
            summary = "Prescoring and credit conditions calculation",
            description = "Creates a credit statement and returns a list of possible credit conditions"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully calculated credit conditions"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.calculateLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("/offer")
    @Operation(
            summary = "Select a credit offer",
            description = "Allows the selection of one credit offer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully selected the credit offer"),
            @ApiResponse(responseCode = "400", description = "Invalid credit offer data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void selectOffer(LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
