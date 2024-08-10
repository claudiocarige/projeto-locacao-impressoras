package br.com.copyimagem.ms_help_desk.infra.controllers;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping( "/api/v1/help-desk" )
public class TicketController {

    private final TicketService ticketService;

    public TicketController( TicketService ticketService ) { this.ticketService = ticketService; }

    @PostMapping( "/create-ticket" )
    public ResponseEntity< TicketDTO > createTicket( @Valid @RequestBody TicketDTO ticketRequestDTO ) {
        return ResponseEntity.ok( ticketService.createTicket( ticketRequestDTO ) );
    }

    @GetMapping( "/get-all-tickets" )
    public ResponseEntity< List< TicketDTO > > getAllTickets() {
        return ResponseEntity.ok( ticketService.getAllTickets() );
    }

    @GetMapping( "/get-ticket/{id}" )
    public ResponseEntity< TicketDTO > getTicketById( @PathVariable Long id ) {
        return ResponseEntity.ok( ticketService.getTicketById( id ) );
    }

    @PatchMapping( "/update-ticket/{id}" )
    public ResponseEntity<Void> updateTicket( @PathVariable Long id,
                                              @RequestParam("ticketType") TicketType ticketType ) {
        ticketService.updateTicketsByType( id, ticketType );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping( "/update-status/{id}" )
    public ResponseEntity<Void> updateStatus( @PathVariable Long id,
                                              @RequestParam("ticketStatus") TicketStatus ticketStatus ) {
        ticketService.updateTicketsByStatus( id, ticketStatus );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping( "/delete-ticket/{id}" )
    public ResponseEntity<Void> deleteTicket( @PathVariable Long id ) {
        ticketService.deleteTicket( id );
        return ResponseEntity.noContent().build();
    }
}
