package br.com.copyimagem.ms_user_service.core.usecases.impl;

import br.com.copyimagem.ms_user_service.core.domain.entities.User;
import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;
import br.com.copyimagem.ms_user_service.core.dtos.UserCreateResponse;
import br.com.copyimagem.ms_user_service.core.dtos.UserRequestDTO;
import br.com.copyimagem.ms_user_service.core.usecases.ConvertEntitiesAndDTOs;
import br.com.copyimagem.ms_user_service.infra.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith( MockitoExtension.class )
class UserServiceImplTest {

    private User user;

    private UserRequestDTO userRequestDTO;

    private UserCreateResponse userCreateResponse;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConvertEntitiesAndDTOs convertEntitiesAndDTOs;

    @Mock
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp() {

        startEntity();
    }

    @Test
    @DisplayName( "Should save a new User" )
    void shouldSaveANewUser() {

        when( userRepository.save( user ) ).thenReturn( user );
        when( userRepository.findByEmail( "ccarige@gmail.com" ) ).thenReturn( null );
        when( convertEntitiesAndDTOs.convertDTOOrEntity( userRequestDTO, User.class ) ).thenReturn( user );
        when( convertEntitiesAndDTOs.convertDTOOrEntity( user, UserCreateResponse.class ) ).thenReturn( userCreateResponse );
        when( bcryptPasswordEncoder.encode( userRequestDTO.getPassword() ) ).thenReturn( "1A2B3C4D5E6F-encoded" );

        UserCreateResponse result = userServiceImpl.save( userRequestDTO );

        assertEquals( userCreateResponse, result );
        assertEquals( userCreateResponse.getEmail(), result.getEmail() );
        assertEquals( userCreateResponse.getProfiles(), result.getProfiles() );

        verify( userRepository, times( 1 ) ).save( user );
        verify( userRepository, times( 1 ) ).findByEmail( "ccarige@gmail.com" );
    }

    private void startEntity() {

        user = new User();
        user.setId( 1L );
        user.setName( "Claudio Carigé" );
        user.setEmail( "ccarige@gmail.com" );
        user.setProfiles( Set.of( ProfileEnum.ROLE_CUSTOMER ) );
        user.setPassword( "1A2B3C4D5E6F-encoded" );
        userRequestDTO = new UserRequestDTO( "Claudio Carigé",
                "ccarige@gmail.com",
                "1A2B3C4D5E6F",
                Set.of( ProfileEnum.ROLE_CUSTOMER ) );
        userCreateResponse = new UserCreateResponse( 1L,
                "Cláudio Carigé",
                "ccarige@gmail.com",
                Set.of( ProfileEnum.ROLE_CUSTOMER ) );

    }

}