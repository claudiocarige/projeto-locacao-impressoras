package br.com.copyimagem.ms_user_service.core.usecases.impl;

import br.com.copyimagem.ms_user_service.core.usecases.ConvertEntitiesAndDTOs;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class ConvertEntitiesAndDTOsImpl implements ConvertEntitiesAndDTOs {

    private final ModelMapper modelMapper;

    public ConvertEntitiesAndDTOsImpl( ModelMapper modelMapper ) { this.modelMapper = modelMapper; }

    @Override
    public < T, U > U convertDTOOrEntity( T source, Class< U > targetClass ) {

        return modelMapper.map( source, targetClass );
    }
}
