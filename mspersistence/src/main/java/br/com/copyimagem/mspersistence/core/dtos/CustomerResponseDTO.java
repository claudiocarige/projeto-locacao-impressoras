package br.com.copyimagem.mspersistence.core.dtos;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {

    private Long id;

    private String clientName;

    private String primaryEmail;

    private String phoneNumber;

    private Address address;

    private String cpfOrCnpj;

    private String financialSituation;

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        CustomerResponseDTO that = ( CustomerResponseDTO ) o;
        return Objects.equals( id, that.id ) && Objects.equals( primaryEmail, that.primaryEmail ) && Objects.equals( cpfOrCnpj, that.cpfOrCnpj );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, primaryEmail, cpfOrCnpj );
    }

}
