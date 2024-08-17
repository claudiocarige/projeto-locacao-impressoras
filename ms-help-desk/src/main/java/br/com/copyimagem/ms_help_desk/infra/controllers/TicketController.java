package br.com.copyimagem.ms_help_desk.infra.controllers;


import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping( "/api/v1/help-desk" )
public interface TicketController {

    @PostMapping( "/create-ticket" )
    ResponseEntity< TicketDTO > createTicket( @Valid @RequestBody TicketDTO ticketRequestDTO );

    @GetMapping( "/get-all-tickets" )
    ResponseEntity< List< TicketDTO > > getAllTickets();

    @GetMapping( "/get-ticket/{id}" )
    ResponseEntity< TicketDTO > getTicketById( @PathVariable Long id );

    @PatchMapping( "/update-ticket/{id}" )
    ResponseEntity<Void> updateTicket( @PathVariable Long id, @RequestParam("ticketType") TicketType ticketType );

    @PatchMapping( "/update-status/{id}" )
    ResponseEntity<Void> updateStatus( @PathVariable Long id,
                                              @RequestParam("ticketStatus") TicketStatus ticketStatus );

    @PatchMapping( "/update-priority/{id}" )
    ResponseEntity<Void> updatePriority( @PathVariable Long id,
                                                @RequestParam("ticketPriority") TicketPriority ticketPriority );

    @DeleteMapping( "/delete-ticket/{id}" )
    ResponseEntity<Void> deleteTicket( @PathVariable Long id );
}
