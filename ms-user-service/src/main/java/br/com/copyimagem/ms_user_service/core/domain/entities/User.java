package br.com.copyimagem.ms_user_service.core.domain.entities;


import br.com.copyimagem.ms_user_service.core.domain.enums.ProfileEnum;
import jakarta.persistence.*;
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
@Entity
@Table( name = "users" )
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String name;

    private String email;

    private String password;

    private String lastPasswordReset;

    private String codeAuthenticator;

    private Integer loginAttempts;

    private Boolean locked;

    @ElementCollection
    @Enumerated( EnumType.STRING )
    private Set< ProfileEnum > profiles;

    @Override
    public boolean equals( Object obj ) {

        if( this == obj ) return true;
        if( obj == null || getClass() != obj.getClass() ) return false;
        User user = ( User ) obj;
        return Objects.equals( id, user.id )
               && Objects.equals( name, user.name )
               && Objects.equals( email, user.email );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, name, email );
    }

}
