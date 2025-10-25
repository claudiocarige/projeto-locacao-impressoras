package br.com.copyimagem.mspersistence.core.domain.entities;

import br.com.copyimagem.mspersistence.core.domain.AssertionConcern;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Address implements Serializable, AssertionConcern {

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

    public Address( Long id, String street, String number, String city, String state, String country ) {

        this.id = id;
        this.setStreet( street );
        this.setNumber( number );
        this.setCity( city );
        this.setState( state );
        this.setCountry( country );
    }

    public void setStreet( String street ) {
        assertionArgumentNotEmpty( street, "Street cannot be empty" );
        assertionArgumentNotNull( street, "Street cannot be null" );
        this.street = street;
    }

    public void setNumber( String number ) {

        assertionArgumentNotEmpty( number, "Number cannot be empty" );
        assertionArgumentNotNull( number, "Number cannot be null" );
        this.number = number;
    }

    public void setCity( String city ) {

        assertionArgumentNotEmpty( city, "City cannot be empty" );
        assertionArgumentNotNull( city, "City cannot be null" );
        this.city = city;
    }

    public void setState( String state ) {

        assertionArgumentNotEmpty( state, "State cannot be empty" );
        assertionArgumentNotNull( state, "State cannot be null" );
        this.state = state;
    }

    public void setCountry( String country ) {

        assertionArgumentNotEmpty( country, "Country cannot be empty" );
        assertionArgumentNotNull( country, "Country cannot be null" );
        this.country = country;
    }

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
