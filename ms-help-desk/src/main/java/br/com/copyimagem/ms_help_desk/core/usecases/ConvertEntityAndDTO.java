package br.com.copyimagem.ms_help_desk.core.usecases;

import java.util.List;


public interface ConvertEntityAndDTO {

    < T, U > U convertDTOToEntity( T source, Class< U > targetClass );

    < T, U > List< U > convertEntityAndDTOList( List< T > source, Class< U > targetClass );

}
