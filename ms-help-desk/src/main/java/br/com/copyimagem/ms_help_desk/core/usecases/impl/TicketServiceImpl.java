package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.UserRequestDTO;
import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.ms_help_desk.core.exceptions.NoSuchElementException;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import br.com.copyimagem.ms_help_desk.infra.adapters.feignservices.MsPersistenceFeignClientService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final ConvertEntityAndDTOService convertEntityAndDTOService;

    private final MsPersistenceFeignClientService msPersistenceFeignClientService;

    public TicketServiceImpl( TicketRepository ticketRepository, ConvertEntityAndDTOService convertEntityAndDTOService, MsPersistenceFeignClientService msPersistenceFeignClientService ) {

        this.ticketRepository = ticketRepository;
        this.convertEntityAndDTOService = convertEntityAndDTOService;
        this.msPersistenceFeignClientService = msPersistenceFeignClientService;
    }

    @Override
    public TicketDTO getTicketById( Long id ) {

        Ticket ticket = ticketRepository.findById( id ).orElseThrow(
                () -> new NoSuchElementException( "Ticket not found" ) );
        return convertEntityAndDTOService.convertEntityToDTO( ticket );
    }

    @Override
    public List< TicketDTO > getAllTickets() {

        return convertEntityAndDTOService.convertEntityListToDTOList( ticketRepository.findAll() );
    }

    @Override
    public TicketDTO createTicket( TicketDTO ticketDTO ) {

        ticketDTO.setId( null );
        ResponseEntity< UserRequestDTO > userRequestDTO =
                msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getClientName() );
        //TODO criar o serviço de Usuario para buscar o tecnico, por enquanto este userRequestDTO02
        ResponseEntity< UserRequestDTO > userRequestDTO02 =
                msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getTechnicalName() );

        if( userRequestDTO.getBody() == null ) {
            throw new IllegalArgumentException( "Client cannot be null or empty" );
        }
        if( userRequestDTO02.getBody() == null ) {
            throw new IllegalArgumentException( "Technical cannot be null or empty" );
        }
        Ticket ticket = convertEntityAndDTOService.convertDTOToEntity( ticketDTO );
        ticket.setClient_id( userRequestDTO.getBody().id() );
        ticket.setTechnical_id( userRequestDTO02.getBody().id() );
        ticket.setClientName( userRequestDTO.getBody().clientName() );
        ticket.setTechnicalName( userRequestDTO02.getBody().clientName() );
        ticket.setStatus( TicketStatus.OPEN );
        if( ticket.getPriority() == null ) {
            ticket.setPriority( TicketPriority.LOW );
        }
        ticket.setCreatedAt( LocalDateTime.now() );
        return convertEntityAndDTOService.convertEntityToDTO( ticketRepository.save( ticket ) );
    }

    @Override
    public void deleteTicket( Long id ) {

        TicketDTO ticketDTO = getTicketById( id );
        if( !ticketDTO.getStatus().equals( "ERROR" ) ) {
            throw new IllegalArgumentException( "Unable to delete the Ticket" );
        }
        ticketRepository.deleteById( id );
    }

    @Override
    public List< TicketDTO > getTicketsByStatus( TicketStatus status ) {

        return convertEntityAndDTOService.convertEntityListToDTOList( ticketRepository.findByStatus( status ) );
    }

    @Override
    public List< TicketDTO > getTicketsByType( TicketType type ) {

        return convertEntityAndDTOService.convertEntityListToDTOList( ticketRepository.findByType( type ) );
    }

}
