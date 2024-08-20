package br.com.copyimagem.ms_user_service.core.dtos;

import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


public record UserCreateResponse ( Long id,
                                   String name,
                                   String email,
                                   Set< ProfileEnum > profiles
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
