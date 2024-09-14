package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.usecases.interfaces.ConvertObjectToObjectDTOService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConvertObjectToObjectDTOServiceImpl implements ConvertObjectToObjectDTOService {

    private final ModelMapper modelMapper;

    @Autowired
    public ConvertObjectToObjectDTOServiceImpl( ModelMapper modelMapper ) {

        this.modelMapper = modelMapper;
    }

    @Override
    public < T, D > D convertToEntityOrDTO( T source, Class< D > targetClass ) {

        return modelMapper.map( source, targetClass );
    }

    @Override
    public < T, U > List< U > convertEntityAndDTOList( List< T > source, Class< U > targetClass ) {

        return source.stream().map( ticket -> modelMapper.map( ticket, targetClass ) ).toList();
    }

}
