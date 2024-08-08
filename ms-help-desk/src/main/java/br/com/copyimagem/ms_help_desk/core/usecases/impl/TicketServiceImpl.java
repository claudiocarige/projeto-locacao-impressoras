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

        final ResultFeignClient resultFeignClient = getResult( ticketDTO );
        Ticket ticket = convertEntityAndDTOService.convertDTOToEntity( ticketDTO );
        ticket.setClient_id( resultFeignClient.client().getBody().id() );
        ticket.setTechnical_id( resultFeignClient.technical().getBody().id() );
        ticket.setClientName( resultFeignClient.client().getBody().clientName() );
        ticket.setTechnicalName( resultFeignClient.technical().getBody().clientName() );
        ticket.setStatus( TicketStatus.OPEN );
        if( ticket.getPriority() == null ) {
            ticket.setPriority( TicketPriority.LOW );
        }
        ticket.setCreatedAt( LocalDateTime.now() );
        return convertEntityAndDTOService.convertEntityToDTO( ticketRepository.save( ticket ) );
    }

    private ResultFeignClient getResult( TicketDTO ticketDTO ) {

        ResponseEntity< UserRequestDTO > userRequestDTO02;
        ResponseEntity< UserRequestDTO > userRequestDTO;
        try {
            userRequestDTO =
                    msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getClientName() );
        }catch ( FeignException.ServiceUnavailable ex){
            throw new CustomFeignException( 503, "The service is currently unavailable. Please try again later." );
        }catch ( FeignException.NotFound ex){
            throw new CustomFeignException( 404, "Client not found: " + ticketDTO.getClientName() );
        }

        //TODO criar o serviço de Usuario para buscar o tecnico, por enquanto este userRequestDTO02
        try{
        userRequestDTO02 =
                msPersistenceFeignClientService.searchCustomerByParams( "clientname", ticketDTO.getTechnicalName() );
        }catch ( FeignException.ServiceUnavailable ex){
            throw new CustomFeignException( 503, "The service is currently unavailable. Please try again later." );
        }catch ( FeignException.NotFound ex){
            throw new CustomFeignException( 404, "Technical not found: " + ticketDTO.getTechnicalName() );
        }

        if( userRequestDTO.getBody() == null ) {
            throw new IllegalArgumentException( "Client cannot be null or empty" );
        }
        if( userRequestDTO02.getBody() == null ) {
            throw new IllegalArgumentException( "Technical cannot be null or empty" );
        }
        return new ResultFeignClient( userRequestDTO, userRequestDTO02 );
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
