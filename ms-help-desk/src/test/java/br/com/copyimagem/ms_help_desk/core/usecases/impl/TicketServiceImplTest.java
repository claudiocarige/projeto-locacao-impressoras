package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.exceptions.NoSuchElementException;
import br.com.copyimagem.ms_help_desk.infra.adapters.feignservices.MsPersistenceFeignClientService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith( MockitoExtension.class)
class TicketServiceImplTest {


    private TicketDTO ticketDTO;

    private Ticket ticket;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ConvertEntityAndDTOService convertEntityAndDTOService;

    @Mock
    private MsPersistenceFeignClientService msPersistenceFeignClientService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        startEntities();
    }


    @Test
    @DisplayName("Should return a ticketDTO with success")
    void shouldReturnATicketDTOWithSuccess() {
        when(ticketRepository.findById(1L)).thenReturn( Optional.of(ticket));
        when(convertEntityAndDTOService.convertEntityToDTO(ticket)).thenReturn(ticketDTO);
        TicketDTO result = ticketService.getTicketById(1L);
        assertEquals( ticketDTO, result);
        assertEquals( ticketDTO.getId(), result.getId());
    }

    @Test
    @DisplayName("Should return a exception when ticket not found")
    void shouldReturnAExceptionWhenTicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn( Optional.empty());
        try {
            ticketService.getTicketById(1L);
        } catch ( NoSuchElementException e) {
            assertEquals("Ticket not found", e.getMessage());
        }
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

    void startEntities(){
        ticket = new Ticket();
        ticket.setId(1L);
        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setClientName("Client Name");
        ticketDTO.setTechnicalName("Technical Name");
        ticketDTO.setPriority("HIGH");
        ticketDTO.setStatus("OPEN");
        ticketDTO.setType("INCIDENT");
        ticketDTO.setCreatedAt( LocalDateTime.now());
        ticketDTO.setUpdatedAt(LocalDateTime.now());
    }

}