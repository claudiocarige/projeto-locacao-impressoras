package br.com.copyimagem.ms_help_desk.infra.controllers.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import br.com.copyimagem.ms_help_desk.infra.controllers.TicketController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TicketControllerImpl implements TicketController {

    private final TicketService ticketService;

    public TicketControllerImpl( TicketService ticketService ) { this.ticketService = ticketService; }

    @Override
    public ResponseEntity< TicketDTO > createTicket( @Valid @RequestBody TicketDTO ticketRequestDTO ) {
        return ResponseEntity.ok( ticketService.createTicket( ticketRequestDTO ) );
    }

    @Override
    public ResponseEntity< List< TicketDTO > > getAllTickets() {
        return ResponseEntity.ok( ticketService.getAllTickets() );
    }

    @Override
    public ResponseEntity< TicketDTO > getTicketById( @PathVariable Long id ) {
        return ResponseEntity.ok( ticketService.getTicketById( id ) );
    }

    @Override
    public ResponseEntity<Void> updateTicket( @PathVariable Long id,
                                              @RequestParam("ticketType") TicketType ticketType ) {
        ticketService.updateTicketsByType( id, ticketType );
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateStatus( @PathVariable Long id,
                                              @RequestParam("ticketStatus") TicketStatus ticketStatus ) {
        ticketService.updateTicketsByStatus( id, ticketStatus );
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updatePriority( @PathVariable Long id,
                                                @RequestParam("ticketPriority") TicketPriority ticketPriority ) {
        ticketService.updateTicketsByPriority( id, ticketPriority );
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteTicket( @PathVariable Long id ) {
        ticketService.deleteTicket( id );
        return ResponseEntity.noContent().build();
    }
}
