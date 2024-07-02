package br.com.copyimagem.mspersistence.core.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( nullable = false )
    private Long id;

    private String street;

    private String number;

    private String city;

    private String state;

    private String country;

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        Address adress = ( Address ) o;
        return Objects.equals( id, adress.id ) && Objects.equals( street, adress.street );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, street );
    }

}
