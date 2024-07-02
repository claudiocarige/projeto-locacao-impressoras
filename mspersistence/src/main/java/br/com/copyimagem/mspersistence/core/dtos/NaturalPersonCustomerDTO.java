package br.com.copyimagem.mspersistence.core.dtos;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaturalPersonCustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size( max = 60 )
    @NotNull( message = "Name is required" )
    private String clientName;

    @CPF( message = "CPF format is invalid" )
    @NotNull( message = "CPF is required" )
    private String cpf;

    @Pattern( regexp = "^[a-zA-Z0-9._+-]+@[a-z]+\\.[a-z]{2,}(?:\\.[a-z]{2,3})?$", message = "Email format is invalid" )
    @NotNull( message = "EMAIL is required" )
    private String primaryEmail;

    private List< String > emailList = new ArrayList<>();

    @NotNull( message = "Phone number is required" )
    private String phoneNumber;

    @NotNull( message = "Whatsapp is required" )
    private String whatsapp;

    private String bankCode;

    private Address address;

    private String financialSituation;

    private byte payDay;

    private CustomerContract customerContract;

    private List< MultiPrinter > multiPrinterList = new ArrayList<>();

    private List< MonthlyPayment > monthlyPaymentList = new ArrayList<>();

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        NaturalPersonCustomerDTO that = ( NaturalPersonCustomerDTO ) o;
        return Objects.equals( id, that.id ) && Objects.equals( cpf, that.cpf ) && Objects.equals( primaryEmail, that.primaryEmail );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, cpf, primaryEmail );
    }

}