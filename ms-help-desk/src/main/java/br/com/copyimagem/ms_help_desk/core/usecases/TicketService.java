package br.com.copyimagem.ms_help_desk.core.usecases;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;

import java.util.List;


public interface TicketService {

    TicketDTO createTicket( TicketDTO ticket );

    TicketDTO getTicketById( Long id );

    List< TicketDTO > getAllTickets();

    void deleteTicket( Long id );

    void updateTicketsByStatus( Long id, TicketStatus status );

    void updateTicketsByType( Long id, TicketType type );

}