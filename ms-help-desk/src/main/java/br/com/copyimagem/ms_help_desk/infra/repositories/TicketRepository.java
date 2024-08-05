package br.com.copyimagem.ms_help_desk.infra.repositories;

import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketRepository extends JpaRepository< Ticket, Long > {

    List< Ticket > findByStatus( TicketStatus status );


    List< Ticket> findByType( TicketType type );

}
