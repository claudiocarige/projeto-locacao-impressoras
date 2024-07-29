package br.com.copyimagem.mspersistence.core.dtos;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LegalPersonalCustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull( message = "Name is required" )
    private String clientName;

    @CNPJ( message = "CNPJ format is invalid" )
    @NotNull( message = "CNPJ is required" )
    private String cnpj;

    @NotNull( message = "Email is required" )
    @Pattern( regexp = "^[a-zA-Z0-9._+-]+@[a-z]+\\.[a-z]{2,}(?:\\.[a-z]{2,3})?$", message = "Email format is invalid" )
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

    private List< MultiPrinterDTO > multiPrinterList = new ArrayList<>();


}
