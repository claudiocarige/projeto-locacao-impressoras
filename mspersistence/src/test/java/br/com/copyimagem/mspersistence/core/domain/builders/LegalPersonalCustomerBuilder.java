package br.com.copyimagem.mspersistence.core.domain.builders;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.enums.FinancialSituation;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.copyimagem.mspersistence.core.domain.builders.AddressBuilder.oneAddress;
import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;


public class LegalPersonalCustomerBuilder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String clientName;
    private String primaryEmail;
    private List<String> emailList = new ArrayList<>();
    private String phoneNumber;
    private String whatsapp;
    private String bankCode;
    private Address address;
    private FinancialSituation financialSituation;
    private byte payDay;

    private CustomerContract customerContract;
    private List< MultiPrinter > multiPrinterList = new ArrayList<>();
    private String cnpj;

    private LegalPersonalCustomerBuilder(){}

    public static LegalPersonalCustomerBuilder oneLegalPersonalCustomer() {
        LegalPersonalCustomerBuilder builder = new LegalPersonalCustomerBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    private static void initializeDefaultData(LegalPersonalCustomerBuilder builder) {
        builder.id = 1L;
        builder.clientName = "Claudio Carigé";
        builder.primaryEmail = "carige@mail.com";
        builder.emailList = Arrays.asList("mail1@mail.com","mail2@mail.com");
        builder.phoneNumber = "7132104567";
        builder.whatsapp = "71998987878";
        builder.bankCode = "123";
        builder.address = oneAddress().now();
        builder.financialSituation = FinancialSituation.PAGO;
        builder.payDay = 5;
        builder.customerContract = new CustomerContract();
        builder.multiPrinterList = Arrays.asList(oneMultiPrinter().now());
        builder.cnpj = "14.124.420/0001-94";
    }

    public LegalPersonalCustomerBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LegalPersonalCustomerBuilder withClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public LegalPersonalCustomerBuilder withPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
        return this;
    }

    public LegalPersonalCustomerBuilder withEmailList(String... emailList) {
        this.emailList = Arrays.asList(emailList);
        return this;
    }

    public LegalPersonalCustomerBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public LegalPersonalCustomerBuilder withWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
        return this;
    }

    public LegalPersonalCustomerBuilder withBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public LegalPersonalCustomerBuilder withAdress(Address adress) {
        this.address = adress;
        return this;
    }

    public LegalPersonalCustomerBuilder withFinancialSituation(FinancialSituation financialSituation) {
        this.financialSituation = financialSituation;
        return this;
    }

    public LegalPersonalCustomerBuilder withPayDay(byte payDay) {
        this.payDay = payDay;
        return this;
    }

    public LegalPersonalCustomerBuilder withCustomerContract(CustomerContract customerContract) {
        this.customerContract = customerContract;
        return this;

    }

    public LegalPersonalCustomerBuilder withListaMultiPrinterList(List<MultiPrinter> multiPrinterList) {
        this.multiPrinterList = multiPrinterList;
        return this;
    }

    public LegalPersonalCustomerBuilder withCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public LegalPersonalCustomer nowCustomerPJ() {
        LegalPersonalCustomer customer = new LegalPersonalCustomer(cnpj);
        customer.setId(id);
        customer.setClientName(clientName);
        customer.setPrimaryEmail(primaryEmail);
        customer.setEmailList(emailList);
        customer.setPhoneNumber(phoneNumber);
        customer.setWhatsapp(whatsapp);
        customer.setBankCode(bankCode);
        customer.setAddress(address);
        customer.setFinancialSituation(financialSituation);
        customer.setPayDay(payDay);
        customer.setCustomerContract(customerContract);
        customer.addMultiPrinter(oneMultiPrinter().now());
        return customer;
    }

    public LegalPersonalCustomerDTO nowCustomerPJDTO(){

        return new LegalPersonalCustomerDTO(
                id,
                clientName,
                cnpj,
                primaryEmail,
                emailList,
                phoneNumber,
                whatsapp,
                bankCode,
                address,
                financialSituation.name(),
                payDay,
                customerContract,
                List.of( oneMultiPrinter().nowDTO() )
        );
    }
}
