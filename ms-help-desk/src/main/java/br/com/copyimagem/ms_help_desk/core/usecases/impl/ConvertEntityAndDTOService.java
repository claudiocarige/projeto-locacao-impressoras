package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConvertEntityAndDTOService {

    private final ModelMapper modelMapper;

    public ConvertEntityAndDTOService( ModelMapper modelMapper ) { this.modelMapper = modelMapper; }

    public TicketDTO convertEntityToDTO( Ticket ticket ) {

        return modelMapper.map( ticket, TicketDTO.class );
    }

    public Ticket convertDTOToEntity( TicketDTO ticketDTO ) {

        return modelMapper.map( ticketDTO, Ticket.class );
    }

    public List< TicketDTO > convertEntityListToDTOList( List< Ticket > tickets ) {

        return tickets.stream().map( ticket -> modelMapper.map( ticket, TicketDTO.class ) ).toList();
    }

}
