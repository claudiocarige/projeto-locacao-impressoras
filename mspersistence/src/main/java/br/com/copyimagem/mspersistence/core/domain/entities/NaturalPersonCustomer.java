package br.com.copyimagem.mspersistence.core.domain.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class NaturalPersonCustomer extends Customer implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Column( unique = true, length = 14 )
    private String cpf;

    public NaturalPersonCustomer( String cpf ) {

        super();
        if( cpf == null || cpf.length() != 14 ) {
            throw new IllegalArgumentException( "Invalid CPF" );
        }
        this.cpf = cpf;
    }

    @Override
    public boolean equals( Object o ) {

        return super.equals( o );
    }

    @Override
    public int hashCode() { return super.hashCode(); }

}
