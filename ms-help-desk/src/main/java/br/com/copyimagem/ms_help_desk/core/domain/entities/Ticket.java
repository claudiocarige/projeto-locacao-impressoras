package br.com.copyimagem.ms_help_desk.core.domain.entities;

import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketPriority;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketStatus;
import br.com.copyimagem.ms_help_desk.core.domain.enums.TicketType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    @Enumerated( EnumType.STRING )
    private TicketStatus status;

    @Enumerated( EnumType.STRING )
    private TicketPriority priority;

    @Enumerated( EnumType.STRING )
    private TicketType type;

    @JsonFormat( pattern = "dd-MM-yyyy HH:mm:ss" )
    private LocalDateTime createdAt;

    @JsonFormat( pattern = "dd-MM-yyyy HH:mm:ss" )
    private LocalDateTime updatedAt;

    @JsonFormat( pattern = "dd-MM-yyyy HH:mm:ss" )
    private LocalDateTime closedAt;

    private String clientName;
    private String technicalName;
    private Long client_id;
    private Long technical_id;

}
