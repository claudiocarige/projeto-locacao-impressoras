package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.infra.adapters.feignservices.MsPersistenceFeignClientService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith( MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ConvertEntityAndDTOService convertEntityAndDTOService;

    @Mock
    private MsPersistenceFeignClientService msPersistenceFeignClientService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void getTicketById() {

    }

    @Test
    void getAllTickets() {

    }

    @Test
    void createTicket() {

    }

    @Test
    void deleteTicket() {

    }

    @Test
    void getTicketsByStatus() {

    }

    @Test
    void getTicketsByType() {

    }

}