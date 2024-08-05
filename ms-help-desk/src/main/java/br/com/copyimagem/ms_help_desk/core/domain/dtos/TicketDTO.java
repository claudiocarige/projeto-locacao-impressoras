package br.com.copyimagem.ms_help_desk.core.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private Long id;

    private String title;

    private String description;

    private String status;

    private String priority;

    private String type;

    private String createdAt;

    private String updatedAt;

    private String closedAt;

    @NotNull( message = "Client name is required" )
    private String clientName;

    @NotNull( message = "Technical name is required" )
    private String technicalName;

}
