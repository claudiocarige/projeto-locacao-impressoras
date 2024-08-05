package br.com.copyimagem.ms_help_desk.core.domain.entities;

import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private String clientName;
    private String technicalName;
    private Long client_id;
    private Long technical_id;

}
