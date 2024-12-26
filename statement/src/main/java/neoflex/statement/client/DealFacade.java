package neoflex.statement.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.statement.annotation.FeignRetryable;
import neoflex.statement.dto.LoanOfferDto;
import neoflex.statement.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealFacade implements DealClient {
    private final DealClient dealClient;

    @Override
    @FeignRetryable
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
        return executeRequest(() -> dealClient.getLoanOffers(loanStatementRequestDto));
    }
    @Override
    @FeignRetryable
    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.debug("Sending request to select offer with data: {}", loanOfferDto);
        executeRequest(() -> {
            dealClient.selectOffer(loanOfferDto);
            return Optional.empty();
        });
    }

    private <T> T executeRequest(Supplier<T> executor) {
        try {
            T result = executor.get();
            log.debug("Response from deal service received successfully, return data: {}", result);
            return result;
        } catch (FeignException e) {
            log.error("Request failed due to error: {}", e.getMessage());
            throw e;
        }
    }
}
