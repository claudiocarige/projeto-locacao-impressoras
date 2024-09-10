package br.com.copyimagem.ms_user_service.infra.controllers;

import br.com.copyimagem.ms_user_service.core.dtos.UserCreateResponse;
import br.com.copyimagem.ms_user_service.core.dtos.UserRequestDTO;
import br.com.copyimagem.ms_user_service.core.usecases.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserControlerImpl{

    private final UserService userService;

    public UserControlerImpl( UserService userService ) { this.userService = userService; }

    @PostMapping( "/create-user" )
    public ResponseEntity< UserCreateResponse > createUser(@RequestBody UserRequestDTO userRequestDTO ) {
        return ResponseEntity.ok( userService.save( userRequestDTO ) );
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }
}
