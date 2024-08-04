package br.com.copyimagem.ms_help_desk.infra.repositories;

import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository< Ticket, Long> {
}
