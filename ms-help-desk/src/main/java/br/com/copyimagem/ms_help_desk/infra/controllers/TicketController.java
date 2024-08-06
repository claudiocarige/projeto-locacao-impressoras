package br.com.copyimagem.ms_help_desk.infra.controllers;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.usecases.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping( "/api/v1/help-desk" )
public class TicketController {

    private final TicketService ticketService;

    public TicketController( TicketService ticketService ) { this.ticketService = ticketService; }

    @PostMapping( "/create-ticket" )
    public ResponseEntity< TicketDTO > createTicket( @Valid @RequestBody TicketDTO ticketRequestDTO ) {
        return ResponseEntity.ok( ticketService.createTicket( ticketRequestDTO ) );
    }

    @GetMapping( "/get-ticket/{id}" )
    public ResponseEntity< TicketDTO > getTicketById( @PathVariable Long id ) {
        return ResponseEntity.ok( ticketService.getTicketById( id ) );
    }

    @GetMapping( "/teste" )
    public ResponseEntity< String > teste() {
        return ResponseEntity.ok( "Teste" );
    }
}
