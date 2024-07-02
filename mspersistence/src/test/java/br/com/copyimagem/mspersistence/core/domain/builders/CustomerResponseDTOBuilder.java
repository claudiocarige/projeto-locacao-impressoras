package br.com.copyimagem.mspersistence.core.domain.builders;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;

import static br.com.copyimagem.mspersistence.core.domain.builders.AddressBuilder.oneAddress;


public class CustomerResponseDTOBuilder {
    private Long id;
    private String clientName;
    private String primaryEmail;
    private String phoneNumber;
    private Address address;
    private String cpfOrCnpj;
    private String financialSituation;

    private CustomerResponseDTOBuilder(){}

    public static CustomerResponseDTOBuilder oneCustomerResponseDTO() {
        CustomerResponseDTOBuilder builder = new CustomerResponseDTOBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    private static void initializeDefaultData(CustomerResponseDTOBuilder builder) {
        builder.id = 1L;
        builder.clientName = "Claudio Carigé";
        builder.primaryEmail = "carige@mail.com";
        builder.phoneNumber = "7132104567";
        builder.address = oneAddress().now();
        builder.cpfOrCnpj = "";
        builder.financialSituation = "PAGO";
    }

    public CustomerResponseDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CustomerResponseDTOBuilder withClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public CustomerResponseDTOBuilder withPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
        return this;
    }

    public CustomerResponseDTOBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerResponseDTOBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CustomerResponseDTOBuilder withCpfOrCnpj(String cpfOrCnpj) {
        this.cpfOrCnpj = cpfOrCnpj;
        return this;
    }

    public CustomerResponseDTOBuilder withFinancialSituation(String financialSituation) {
        this.financialSituation = financialSituation;
        return this;
    }

    public CustomerResponseDTO now() {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(id);
        customerResponseDTO.setClientName(clientName);
        customerResponseDTO.setPrimaryEmail(primaryEmail);
        customerResponseDTO.setPhoneNumber(phoneNumber);
        customerResponseDTO.setAddress(address);
        customerResponseDTO.setCpfOrCnpj(cpfOrCnpj);
        customerResponseDTO.setFinancialSituation(financialSituation);
        return customerResponseDTO;
    }
}
