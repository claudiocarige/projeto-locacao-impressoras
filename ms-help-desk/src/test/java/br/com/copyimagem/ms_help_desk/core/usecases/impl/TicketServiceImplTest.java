package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.dtos.UserRequestDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.exceptions.CustomFeignException;
import br.com.copyimagem.ms_help_desk.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.ms_help_desk.core.exceptions.NoSuchElementException;
import br.com.copyimagem.ms_help_desk.core.usecases.ConvertEntityAndDTO;
import br.com.copyimagem.ms_help_desk.infra.adapters.feignservices.MsPersistenceFeignClientService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith( MockitoExtension.class )
class TicketServiceImplTest {


    private TicketDTO ticketDTO;

    private Ticket ticket;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ConvertEntityAndDTO convertEntityAndDTOService;

    @Mock
    private MsPersistenceFeignClientService msPersistenceFeignClientService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {

        startEntities();
    }


    @Test
    @DisplayName( "Should return a ticketDTO with success" )
    void shouldReturnATicketDTOWithSuccess() {

        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        when( convertEntityAndDTOService.convertDTOOrEntity( ticket , TicketDTO.class) ).thenReturn( ticketDTO );
        TicketDTO result = ticketService.getTicketById( 1L );
        assertEquals( ticketDTO, result );
        assertEquals( ticketDTO.getId(), result.getId() );
    }

    @Test
    @DisplayName( "Should return a exception when ticket not found" )
    void shouldReturnAExceptionWhenTicketNotFound() {

        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.empty() );
        try {
            ticketService.getTicketById( 1L );
        } catch( NoSuchElementException e ) {
            assertEquals( "Ticket not found", e.getMessage() );
        }
    }

    @Test
    @DisplayName( "Should return a list of ticketDTO." )
    void shouldReturnAListOfTicketDTO() {

        when( ticketRepository.findAll() ).thenReturn( List.of( ticket ) );
        when( convertEntityAndDTOService
                   .convertEntityAndDTOList( List.of( ticket ), TicketDTO.class ) ).thenReturn( List.of( ticketDTO ) );
        List< TicketDTO > result = ticketService.getAllTickets();
        assertEquals( List.of( ticketDTO ), result );
        assertEquals( ticketDTO.getId(), result.get( 0 ).getId() );
    }

    @Test
    @DisplayName( "Should create a ticket with success" )
    void shouldCreateATicketWithSuccess() {

        ResponseEntity< UserRequestDTO > clientResponse = ResponseEntity.ok( new UserRequestDTO( 1L, "Client Name" ) );
        ResponseEntity< UserRequestDTO > technicalResponse =
                                                        ResponseEntity.ok( new UserRequestDTO( 2L, "Technical Name" ) );

        when( msPersistenceFeignClientService
                      .searchCustomerByParams( "clientname", ticketDTO.getClientName() ) ).thenReturn( clientResponse );
        when( msPersistenceFeignClientService
                .searchCustomerByParams( "clientname", ticketDTO.getTechnicalName() ) ).thenReturn( technicalResponse );
        when( convertEntityAndDTOService.convertDTOOrEntity( ticketDTO, Ticket.class) ).thenReturn( ticket );
        when( ticketRepository.save( ticket ) ).thenReturn( ticket );
        when( convertEntityAndDTOService.convertDTOOrEntity( ticket , TicketDTO.class) ).thenReturn( ticketDTO );
        TicketDTO result = ticketService.createTicket( ticketDTO );

        assertNotNull( result );
        assertEquals( 1L, result.getId() );
        assertEquals( "Client Name", result.getClientName() );

        verify( msPersistenceFeignClientService, times( 1 ) ).searchCustomerByParams( "clientname", "Client Name" );
        verify( msPersistenceFeignClientService, times( 1 ) ).searchCustomerByParams( "clientname", "Technical Name" );

    }

    @Test
    @DisplayName( "Should return a exception when client not found" )
    void shouldReturnAExceptionWhenClientNotFound() {

        ResponseEntity< UserRequestDTO > clientResponse = ResponseEntity.ok( new UserRequestDTO( 1L, "Client Name" ) );
        ResponseEntity< UserRequestDTO > technicalResponse = ResponseEntity.
                                                                      ok( new UserRequestDTO( 2L, "Technical Name" ) );

        when( msPersistenceFeignClientService
                .searchCustomerByParams( "clientname", ticketDTO.getClientName() ) ).thenReturn( clientResponse );
        when( msPersistenceFeignClientService
                .searchCustomerByParams( "clientname", ticketDTO.getTechnicalName() ) ).thenReturn( technicalResponse );
        when( convertEntityAndDTOService.convertDTOOrEntity( ticketDTO, Ticket.class ) ).thenReturn( ticket );
        when( ticketRepository.save( ticket ) ).thenReturn( ticket );
        when( convertEntityAndDTOService.convertDTOOrEntity( ticket , TicketDTO.class) ).thenReturn( ticketDTO );

        try {
            ticketService.createTicket( ticketDTO );
        } catch( CustomFeignException e ) {
            assertEquals( "Client not found", e.getMessage() );
            System.out.println( e.getMessage() );
        }
    }

    @Test
    @DisplayName( "Should delete a ticket with success" )
    void shouldDeleteATicketWithSucess() {

        ticketDTO.setStatus( "ERROR" );
        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        when( ticketService.getTicketById( 1L ) ).thenReturn( ticketDTO );
        ticketService.deleteTicket( 1L );
        verify( ticketRepository, times( 1 ) ).deleteById( 1L );

    }

    @Test
    @DisplayName( "Should return a exception when ticket is not in error status" )
    void shouldReturnAExceptionWhenTicketIsNotInErrorStatus() {

        ticketDTO.setStatus( "OPEN" );
        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        when( ticketService.getTicketById( 1L ) ).thenReturn( ticketDTO );
        IllegalArgumentException exception = assertThrows(
                                          IllegalArgumentException.class, () -> { ticketService.deleteTicket( 1L ); } );
        assertEquals( "Unable to delete the Ticket", exception.getMessage() );
    }

    @Test
    @DisplayName( "Should Update a Ticket Status" )
    void shouldUpdateTicketStatus() {

        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        ticketService.updateTicketsByStatus( 1L, TicketStatus.IN_PROGRESS );

        assertEquals( ticket.getStatus(), TicketStatus.IN_PROGRESS );
        assertNotNull( ticket.getUpdatedAt() );
        verify( ticketRepository, times( 1 ) ).save( ticket );
    }

    @Test
    @DisplayName( "Should return a exception when status is Closed" )
    void shouldReturnAExceptionWhenStatusIsClosed() {

        ticket.setStatus( TicketStatus.CLOSED );
        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> ticketService.updateTicketsByStatus( 1L, TicketStatus.CLOSED ) );
        assertEquals( "Ticket is already closed", exception.getMessage() );
        verify( ticketRepository, never() ).save( any( Ticket.class ) );
    }

    @Test
    @DisplayName( "Should Update a Ticket Type" )
    void shouldUpdateATicketsType() {

        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        ticketService.updateTicketsByType( 1L, TicketType.REFILL );

        assertEquals( ticket.getType(), TicketType.REFILL );
        assertNotNull( ticket.getUpdatedAt() );
        verify( ticketRepository, times( 1 ) ).save( ticket );

    }

    @Test
    @DisplayName( "Should update a Ticket Priority" )
    void shouldUpdateATicketsPriority() {

        when( ticketRepository.findById( 1L ) ).thenReturn( Optional.of( ticket ) );
        ticketService.updateTicketsByPriority( 1L, TicketPriority.HIGH );

        assertEquals( ticket.getPriority(), TicketPriority.HIGH );
        assertNotNull( ticket.getUpdatedAt() );
        verify( ticketRepository, times( 1 ) ).save( ticket );
    }

    void startEntities() {

        ticket = new Ticket();
        ticket.setId( 1L );
        ticket.setStatus( TicketStatus.OPEN );
        ticketDTO = new TicketDTO();
        ticketDTO.setId( 1L );
        ticketDTO.setClientName( "Client Name" );
        ticketDTO.setTechnicalName( "Technical Name" );
        ticketDTO.setPriority( "HIGH" );
        ticketDTO.setStatus( "OPEN" );
        ticketDTO.setType( "INCIDENT" );
        ticketDTO.setCreatedAt( LocalDateTime.now() );
        ticketDTO.setUpdatedAt( LocalDateTime.now() );
    }

}