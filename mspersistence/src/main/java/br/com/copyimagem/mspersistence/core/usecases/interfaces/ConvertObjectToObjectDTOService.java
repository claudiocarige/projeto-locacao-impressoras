package br.com.copyimagem.mspersistence.core.usecases.interfaces;

import java.util.List;


public interface ConvertObjectToObjectDTOService {

    < T, U > U convertToEntityOrDTO( T source, Class< U > targetClass );

    < T, U > List< U > convertEntityAndDTOList( List< T > source, Class< U > targetClass );

}
