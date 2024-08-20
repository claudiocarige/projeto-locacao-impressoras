package br.com.copyimagem.ms_user_service.core.usecases;

import br.com.copyimagem.ms_user_service.core.domain.entities.User;
import br.com.copyimagem.ms_user_service.core.dtos.UserCreateResponse;
import br.com.copyimagem.ms_user_service.core.dtos.UserLoginResponseDTO;
import br.com.copyimagem.ms_user_service.core.dtos.UserRequestDTO;
import br.com.copyimagem.ms_user_service.core.dtos.UserResponseDTO;

import java.util.List;


public interface UserService {

    UserCreateResponse save( UserRequestDTO userRequestDTO );

    UserLoginResponseDTO findByEmail( String email );

    User findById( Long id );

    List< UserResponseDTO > findAll();

    void delete( Long id );

    UserResponseDTO update( User user );

}
