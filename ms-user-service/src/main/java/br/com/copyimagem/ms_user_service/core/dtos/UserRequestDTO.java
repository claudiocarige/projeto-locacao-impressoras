package br.com.copyimagem.ms_user_service.core.dtos;


import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank( message = "The NAME cannot be empty" )
    @NotNull( message = "The NAME field is required!" )
    @Size( min = 6, max = 50, message = "Name must contain between 36 and 50 characters" )
    private String name;

    @Email( message = "Invalid email" )
    @NotNull( message = "The EMAIL field is required!" )
    @NotBlank( message = "Email cannot be empty" )
    @Size( min = 6, max = 50, message = "Email must contain between 6 and 50 characters" )
    private String email;

    @Size( min = 8, max = 20, message = "Password must contain between 8 and 20 characters" )
    @NotBlank( message = "The PASSWORD cannot be empty" )
    @NotNull( message = "The PASSWORD field is required!" )
    @Pattern( regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$", message = "Password format is invalid" )
    private String password;

    Set< ProfileEnum > profiles;

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        UserRequestDTO that = ( UserRequestDTO ) o;
        return Objects.equals( name, that.name )
                && Objects.equals( email, that.email )
                && Objects.equals( password, that.password );
    }

    @Override
    public int hashCode() {

        return Objects.hash( name, email, password );
    }

}
