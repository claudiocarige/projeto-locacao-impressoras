package br.com.copyimagem.ms_user_service.core.usecases;

public interface ConvertEntitiesAndDTOs {

    < T, U > U convertDTOOrEntity( T source, Class< U > targetClass );
}
