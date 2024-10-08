package br.com.copyimagem.mspersistence.core.domain.entities;


import br.com.copyimagem.mspersistence.core.domain.enums.FinancialSituation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorColumn( name = "dtype" )
public abstract class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( nullable = false )
    private Long id;

    private String clientName;

    @Column( unique = true )
    private String primaryEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    private List< String > emailList = new ArrayList<>();

    private String phoneNumber;

    private String whatsapp;

    private String bankCode;

    @OneToOne
    @JoinColumn( name = "address_id" )
    private Address address;

    @Enumerated( EnumType.STRING )
    private FinancialSituation financialSituation;

    private byte payDay;

    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "contract_id" )
    private CustomerContract customerContract;

    @Setter( AccessLevel.NONE )
    @JsonIgnore
    @OneToMany( fetch = FetchType.EAGER, mappedBy = "customer" )
    private List< MultiPrinter > multiPrinterList = new ArrayList<>();


    public void addMultiPrinter( MultiPrinter multiPrinter ) {

        multiPrinterList.add( multiPrinter );
    }

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        Customer customer = ( Customer ) o;
        return Objects.equals( id, customer.id ) && Objects.equals( primaryEmail, customer.primaryEmail );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, primaryEmail );
    }

}