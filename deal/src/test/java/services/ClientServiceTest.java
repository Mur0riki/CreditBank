package services;

import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.mapping.ClientMapper;
import neoflex.deal.model.Client;
import neoflex.deal.repositories.ClientRepositories;
import neoflex.deal.services.ClientService;
import neoflex.deal.services.serviceImpl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepositories clientRepositories;

    @Mock
    private ClientMapper clientMapper;


    private ClientService clientService;

    private LoanStatementRequestDto loanStatementRequestDto;
    private Client client;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(clientRepositories, clientMapper);

        loanStatementRequestDto =
                LoanStatementRequestDto.builder()
                        .email("ivan.ivanov@example.com")
                        .firstName("Ivan")
                        .lastName("Ivanov")
                        .build();

        client =
                Client.builder().email("ivan.ivanov@example.com").firstName("Ivan").lastName("Ivanov").build();
    }

    @Test
    void testCreateClient() {
        when(clientMapper.toClient(loanStatementRequestDto)).thenReturn(client);
        when(clientRepositories.save(client)).thenReturn(client);

        Client createdClient = clientService.createClient(loanStatementRequestDto);

        assertNotNull(createdClient);
        assertEquals("Ivan", createdClient.getFirstName());
        assertEquals("Ivanov", createdClient.getLastName());
        assertEquals("ivan.ivanov@example.com", createdClient.getEmail());

        verify(clientMapper).toClient(loanStatementRequestDto);
        verify(clientRepositories).save(client);
    }
}
