package br.com.copyimagem.ms_user_service.core.dtos;

import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


public record UserResponseDTO(  Long id,
                                String name,
                                String email,
                                String lastPasswordReset,
                                String codeAuthenticator,
                                Integer loginAttempts,
                                Boolean locked,
                                Set< ProfileEnum > profiles

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
