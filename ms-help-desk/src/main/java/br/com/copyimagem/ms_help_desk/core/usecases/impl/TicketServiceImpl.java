package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.ResultFeignClient;
import br.com.copyimagem.ms_help_desk.core.domain.dtos.UserRequestDTO;
import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.exceptions.CustomFeignException;
import br.com.copyimagem.ms_help_desk.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.ms_help_desk.core.exceptions.NoSuchElementException;
import br.com.copyimagem.ms_help_desk.core.usecases.ConvertEntityAndDTO;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import br.com.copyimagem.ms_help_desk.infra.adapters.feignservices.MsPersistenceFeignClientService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final ConvertEntityAndDTO convertEntityAndDTO;

    private final MsPersistenceFeignClientService msPersistenceFeignClientService;

    public TicketServiceImpl( TicketRepository ticketRepository, ConvertEntityAndDTO convertEntityAndDTO, MsPersistenceFeignClientService msPersistenceFeignClientService ) {

        this.ticketRepository = ticketRepository;
        this.convertEntityAndDTO = convertEntityAndDTO;
        this.msPersistenceFeignClientService = msPersistenceFeignClientService;
    }

    @Override
    public TicketDTO getTicketById( Long id ) {

        return convertEntityAndDTO.convertDTOToEntity( findById( id ), TicketDTO.class );
    }

    @Override
    public List< TicketDTO > getAllTickets() {

        return convertEntityAndDTO.convertEntityAndDTOList( ticketRepository.findAll() , TicketDTO.class );
    }

    @Override
    public TicketDTO createTicket( TicketDTO ticketDTO ) {

        final ResultFeignClient resultFeignClient = callToFeignClient( ticketDTO );
        Ticket ticket = convertEntityAndDTO.convertDTOToEntity( ticketDTO, Ticket.class );
        ticket.setClient_id( resultFeignClient.client().getBody().id() );
        ticket.setTechnical_id( resultFeignClient.technical().getBody().id() );
        ticket.setClientName( resultFeignClient.client().getBody().clientName() );
        ticket.setTechnicalName( resultFeignClient.technical().getBody().clientName() );
        ticket.setStatus( TicketStatus.OPEN );
        if( ticket.getPriority() == null ) {
            ticket.setPriority( TicketPriority.LOW );
        }
        ticket.setCreatedAt( LocalDateTime.now() );
        return convertEntityAndDTO.convertDTOToEntity( ticketRepository.save( ticket ) , TicketDTO.class );
    }

    private ResultFeignClient callToFeignClient( TicketDTO ticketDTO ) {

        ResponseEntity< UserRequestDTO > userRequestDTO02;
        ResponseEntity< UserRequestDTO > userRequestDTO;
        try {
            userRequestDTO =
                    msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getClientName() );
        } catch( FeignException.ServiceUnavailable ex ) {
            throw new CustomFeignException( "The service is currently unavailable. Please try again later.", 503 );
        } catch( FeignException.NotFound ex ) {
            throw new CustomFeignException( "Customer not found: " + ticketDTO.getClientName().toUpperCase() +
                    " does not exist in the database ", 404 );
        }

        //TODO criar o serviço de Usuario para buscar o tecnico, por enquanto este userRequestDTO02
        try {
            userRequestDTO02 =
                   msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getTechnicalName() );
        } catch( FeignException.ServiceUnavailable ex ) {
            throw new CustomFeignException( "The service is currently unavailable. Please try again later.", 503 );
        } catch( FeignException.NotFound ex ) {
            throw new CustomFeignException( "Technical not found: " + ticketDTO.getTechnicalName().toUpperCase() +
                                            " does not exist in the database ", 404 );
        }

        return new ResultFeignClient( userRequestDTO, userRequestDTO02 );
    }

    @Override
    public void deleteTicket( Long id ) {

        TicketDTO ticketDTO = getTicketById( id );
        if( ! ticketDTO.getStatus().equals( "ERROR" ) ) {
            throw new IllegalArgumentException( "Unable to delete the Ticket" );
        }
        ticketRepository.deleteById( id );
    }

    @Override
    public void updateTicketsByStatus( Long id, TicketStatus status ) {

        validateStatus( status );
        Ticket ticket = findById( id );
        isTicketClosed( ticket );
        updateTimeStamps( status, ticket );
        ticket.setStatus( status );
        ticketRepository.save( ticket );

    }

    @Override
    public void updateTicketsByType( Long id, TicketType type ) {

        validateType( type );
        Ticket ticket = findById( id );
        isTicketClosed( ticket );
        ticket.setType( type );
        ticket.setUpdatedAt( LocalDateTime.now() );
        ticketRepository.save( ticket );

    }

    @Override
    public void updateTicketsByPriority( Long id, TicketPriority priority ) {

        validatePriority( priority );
        Ticket ticket = findById( id );
        isTicketClosed( ticket );
        ticket.setPriority( priority );
        ticket.setUpdatedAt( LocalDateTime.now() );
        ticketRepository.save(  ticket );
    }

    private Ticket findById( Long id ) {

        return ticketRepository.findById( id ).orElseThrow(
                () -> new NoSuchElementException( "Ticket not found" ) );
    }

    private static void updateTimeStamps( TicketStatus status, Ticket ticket ) {

        if( status.equals( TicketStatus.IN_PROGRESS ) ) {
            ticket.setUpdatedAt( LocalDateTime.now() );
        } else if( status.equals( TicketStatus.CLOSED ) ) {
            ticket.setClosedAt( LocalDateTime.now() );
        }

    }

    private static void isTicketClosed( Ticket ticket ) {

        if( ticket.getStatus().equals( TicketStatus.CLOSED ) ) {
            throw new IllegalArgumentException( "Ticket is already closed" );
        }
    }

    private static void validateStatus( TicketStatus status ) {

        if( status != TicketStatus.OPEN &&
            status != TicketStatus.IN_PROGRESS &&
            status != TicketStatus.CLOSED &&
            status != TicketStatus.ERROR ) {
            throw new IllegalArgumentException( "Invalid status: " + status );
        }
    }

    private static void validateType( TicketType type ) {

        if( type != TicketType.TECHNICAL_VISIT &&
            type != TicketType.REFILL &&
            type != TicketType.TONER ) {
            throw new IllegalArgumentException( "Invalid type: " + type );
        }
    }

    private static void validatePriority( TicketPriority priority ) {

        if( priority != TicketPriority.LOW &&
            priority != TicketPriority.MEDIUM &&
            priority != TicketPriority.HIGH &&
            priority != TicketPriority.URGENT ) {
            throw new IllegalArgumentException( "Invalid priority: " + priority );
        }
    }

}
