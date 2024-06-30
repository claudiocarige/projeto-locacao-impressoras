package br.com.copyimagem.mspersistence.core.domain.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class LegalPersonalCustomer extends Customer {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column( unique = true, length = 18 )
    private String cnpj;

    public LegalPersonalCustomer( String cnpj ) {

        super();
        if( cnpj == null || cnpj.length() != 18 ) {
            throw new IllegalArgumentException( "Invalid CNPJ" );
        }
        this.cnpj = cnpj;
    }

    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public boolean equals( Object obj ) { return super.equals( obj ); }

}