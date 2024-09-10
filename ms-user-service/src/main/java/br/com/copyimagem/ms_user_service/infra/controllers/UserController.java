package br.com.copyimagem.ms_user_service.infra.controllers;

import br.com.copyimagem.ms_user_service.core.dtos.UserCreateResponse;
import br.com.copyimagem.ms_user_service.core.dtos.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1/user")
public interface UserController {

    @PostMapping( "/create-user" )
    ResponseEntity< UserCreateResponse > createUser( @RequestBody UserRequestDTO userRequestDTO );
}
