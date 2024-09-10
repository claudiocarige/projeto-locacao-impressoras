package br.com.copyimagem.ms_user_service.core.usecases.impl;

import br.com.copyimagem.ms_user_service.core.domain.entities.User;
import br.com.copyimagem.ms_user_service.core.dtos.UserCreateResponse;
import br.com.copyimagem.ms_user_service.core.dtos.UserLoginResponseDTO;
import br.com.copyimagem.ms_user_service.core.dtos.UserRequestDTO;
import br.com.copyimagem.ms_user_service.core.dtos.UserResponseDTO;
import br.com.copyimagem.ms_user_service.core.usecases.ConvertEntitiesAndDTOs;
import br.com.copyimagem.ms_user_service.core.usecases.UserService;
import br.com.copyimagem.ms_user_service.infra.persistence.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final ConvertEntitiesAndDTOs convertEntitiesAndDTOs;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl( ConvertEntitiesAndDTOs convertEntitiesAndDTOs,
                            UserRepository userRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder ) {

        this.convertEntitiesAndDTOs = convertEntitiesAndDTOs;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserCreateResponse save( UserRequestDTO userRequestDTO ) {

        verifyIfEmailExists( userRequestDTO.getEmail(), null );
        User user = convertEntitiesAndDTOs.convert( userRequestDTO, User.class );
        user.setPassword( bCryptPasswordEncoder.encode( userRequestDTO.getPassword() ) );
        user = userRepository.save( user );
        return convertEntitiesAndDTOs.convert( user, UserCreateResponse.class );
    }

    @Override
    public UserLoginResponseDTO findByEmail( String email ) {

        return null;
    }

    @Override
    public User findById( Long id ) {

        return null;
    }

    @Override
    public List< UserResponseDTO > findAll() {

        return List.of();
    }

    @Override
    public void delete( Long id ) {

    }

    @Override
    public UserResponseDTO update( User user ) {

        return null;
    }

    private void verifyIfEmailExists( String email, Long id ) {

        User user = userRepository.findByEmail( email );
        if( user != null && ! user.getId().equals( id ) ) {
            throw new DataIntegrityViolationException( "Email [ " + email + " ] already exists" );
        }

    }

}
