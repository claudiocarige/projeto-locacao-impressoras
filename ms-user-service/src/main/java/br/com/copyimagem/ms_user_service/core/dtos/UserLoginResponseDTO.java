package br.com.copyimagem.ms_user_service.core.dtos;

import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;

import java.io.Serial;
import java.util.Set;


public record UserLoginResponseDTO( String email, String password,
                                    Set< ProfileEnum > profile ) implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
