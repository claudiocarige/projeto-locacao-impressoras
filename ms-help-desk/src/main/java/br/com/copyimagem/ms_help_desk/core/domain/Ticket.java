package br.com.copyimagem.ms_help_desk.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String description;
    private StatusTicket status;
    private PriorityTicket priority;
    private TypeTicket type;
    private Long user_id;
    private Long technician;
    private String createdAt;
    private String updatedAt;
    private String closedAt;

}
