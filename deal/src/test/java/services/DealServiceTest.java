package services;

import feign.FeignException;
import neoflex.deal.dto.*;
import neoflex.deal.exceptionhandling.PrescoringException;
import neoflex.deal.mapping.CreditMapper;
import neoflex.deal.mapping.ScoringDataMapper;
import neoflex.deal.model.Client;
import neoflex.deal.model.Credit;
import neoflex.deal.model.Statement;
import neoflex.deal.model.enumFields.CreditStatus;
import neoflex.deal.model.enumFields.Status;
import neoflex.deal.services.DealService;
import neoflex.deal.services.client.CalculatorClient;
import neoflex.deal.services.serviceImpl.ClientServiceImpl;
import neoflex.deal.services.serviceImpl.CreditServiceImpl;
import neoflex.deal.services.serviceImpl.DealServiceImpl;
import neoflex.deal.services.serviceImpl.StatementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private StatementServiceImpl statementService;

    @Mock
    private CreditServiceImpl creditService;

    @Mock
    private CalculatorClient calculatorClient;

    @Mock
    private ScoringDataMapper scoringDataMapper;

    @Mock
    private CreditMapper creditMapper;

    private DealService dealService;

    LoanStatementRequestDto requestDto;
    Client mockClient;
    Statement mockStatement;

    @BeforeEach
    void setUp() {
        requestDto = LoanStatementRequestDto.builder().build();
        mockClient = new Client();
        mockStatement = new Statement();
        mockStatement.setStatementId(UUID.randomUUID());
        dealService = new DealServiceImpl(clientService, statementService, creditService, calculatorClient, scoringDataMapper, creditMapper);
    }

    @Test
    void testCreateStatementSuccess() {
        List<LoanOfferDto> mockOffers = List.of(LoanOfferDto.builder().build());

        when(clientService.createClient(requestDto)).thenReturn(mockClient);
        when(statementService.createStatement(mockClient)).thenReturn(mockStatement);
        when(calculatorClient.getLoanOffers(requestDto)).thenReturn(mockOffers);

        ResponseEntity<List<LoanOfferDto>> response = dealService.createStatement(requestDto);

        assertNotNull(response);
        assertEquals(mockOffers, response.getBody());
        verify(clientService).createClient(requestDto);
        verify(statementService).createStatement(mockClient);
        verify(calculatorClient).getLoanOffers(requestDto);
    }

    @Test
    void testCreateStatementFailureDueToPrescoring() {

        when(clientService.createClient(requestDto)).thenReturn(mockClient);
        when(statementService.createStatement(mockClient)).thenReturn(mockStatement);

        feign.Response mockResponse =
                feign.Response.builder()
                        .status(400)
                        .request(
                                feign.Request.create(
                                        feign.Request.HttpMethod.GET,
                                        "/calculator",
                                        java.util.Map.of(),
                                        null,
                                        null,
                                        null))
                        .build();

        when(calculatorClient.getLoanOffers(requestDto))
                .thenThrow(FeignException.errorStatus("Bad Request", mockResponse));

        // Act & Assert
        assertThrows(PrescoringException.class, () -> dealService.createStatement(requestDto));
        verify(statementService).saveStatement(mockStatement);
    }

    @Test
    void testSelectOffer() {
        LoanOfferDto offerDto = LoanOfferDto.builder().statementId(UUID.randomUUID()).build();
        Statement mockStatement = new Statement();

        when(statementService.findStatementById(offerDto.getStatementId())).thenReturn(mockStatement);

        dealService.selectOffer(offerDto);

        assertEquals(Status.APPROVED, mockStatement.getStatus());
        assertEquals(offerDto, mockStatement.getAppliedOffer());
        verify(statementService).saveStatement(mockStatement);
    }

    @Test
    void testCalculateSucces() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishRegistrationRequestDto =
                FinishRegistrationRequestDto.builder().build();

        Statement mockStatement = new Statement();
        mockStatement.setStatementId(statementId);

        ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();

        CreditDto creditDto = CreditDto.builder().build();
        Credit mockCredit = new Credit();

        when(statementService.findStatementById(statementId)).thenReturn(mockStatement);
        when(scoringDataMapper.toScoringDataDto(mockStatement, finishRegistrationRequestDto))
                .thenReturn(scoringDataDto);
        when(calculatorClient.getCredit(scoringDataDto)).thenReturn(creditDto);
        when(creditMapper.toCredit(creditDto)).thenReturn(mockCredit);

        dealService.calculate(statementId, finishRegistrationRequestDto);

        verify(statementService).saveStatement(mockStatement);
        verify(creditService).saveCredit(mockCredit);
        assertEquals(CreditStatus.CALCULATED, mockCredit.getCreditStatus());
    }
}
