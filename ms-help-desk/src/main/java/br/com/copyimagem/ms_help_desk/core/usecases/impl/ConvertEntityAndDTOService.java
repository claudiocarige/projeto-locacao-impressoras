package br.com.copyimagem.ms_help_desk.core.usecases.impl;

import br.com.copyimagem.ms_help_desk.core.domain.dtos.TicketDTO;
import br.com.copyimagem.ms_help_desk.core.domain.entities.Ticket;
import br.com.copyimagem.ms_help_desk.core.usecases.ConvertEntityAndDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConvertEntityAndDTOService implements ConvertEntityAndDTO {

    private final ModelMapper modelMapper;

    public ConvertEntityAndDTOService( ModelMapper modelMapper ) { this.modelMapper = modelMapper; }


    @Override
    public < T, U > U convertDTOToEntity( T source, Class< U > targetClass ) {

        return modelMapper.map( source, targetClass );
    }
    @Override
    public < T, U > List< U > convertEntityAndDTOList( List< T > source, Class< U > targetClass ) {

        return source.stream().map( ticket -> modelMapper.map( ticket, targetClass ) ).toList();
    }

}
