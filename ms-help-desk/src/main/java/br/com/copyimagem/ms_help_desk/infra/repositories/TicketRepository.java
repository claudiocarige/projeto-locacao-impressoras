package br.com.copyimagem.ms_help_desk.infra.repositories;

import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


public interface TicketRepository extends JpaRepository< Ticket, Long > {

    @Transactional
    @Modifying
    @Query( "UPDATE Ticket t SET t.type = :type, t.updatedAt = :updatedAt WHERE t.id = :id" )
    int updateTypeById( @Param( "id" ) Long id,
                          @Param( "type" ) TicketType type,
                          @Param( "updatedAt" ) LocalDateTime updatedAt );

    @Transactional
    @Modifying
    @Query(value = "UPDATE ticket SET status = :status, " +
            "updated_at = CASE WHEN :dataField = 'updatedAt' THEN CURRENT_TIMESTAMP ELSE updated_at END, " +
            "closed_at = CASE WHEN :dataField = 'closedAt' THEN CURRENT_TIMESTAMP ELSE closed_at END " +
            "WHERE id = :id", nativeQuery = true)
    int updateStatusById( @Param( "id" ) Long id,
                        @Param( "status" ) String status,
                        @Param( "dataField") String dataField);


    @Transactional
    @Modifying
    @Query( "UPDATE Ticket t SET t.priority = :priority, t.updatedAt = :updatedAt WHERE t.id = :id" )
    int updatePriorityById( @Param( "id" ) Long id,
                          @Param( "priority" ) TicketPriority priority,
                          @Param( "updatedAt" ) LocalDateTime updatedAt );
}
