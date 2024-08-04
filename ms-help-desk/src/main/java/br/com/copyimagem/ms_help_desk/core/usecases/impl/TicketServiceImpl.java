package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import br.com.copyimagem.ms_help_desk.infra.repositories.TicketRepository;

import java.util.List;
import java.util.NoSuchElementException;


public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketDTO createTicket( TicketDTO ticket ) {

        return ticketRepository.save( ticket );
    }

    @Override
    public TicketDTO getTicketById( Long id ) {

        return ticketRepository.findById( id ).orElseThrow( () -> new NoSuchElementException( "Ticket not found" ) );
    }

    @Override
    public List< TicketDTO > getAllTickets() {

        return ticketRepository.findAll();
    }

    @Override
    public void deleteTicket( Long id ) {

        ticketRepository.deleteById( id );
    }

    @Override
    public List< TicketDTO > getTicketsByStatus( TicketStatus status ) {

        return ticketRepository.findByStatus( status );
    }

    @Override
    public List< TicketDTO > getTicketsByType( TicketType type ) {

        return ticketRepository.findByType( type );
    }

}
