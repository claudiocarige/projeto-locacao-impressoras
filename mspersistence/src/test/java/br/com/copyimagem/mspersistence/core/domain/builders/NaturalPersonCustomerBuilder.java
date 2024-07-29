package br.com.copyimagem.mspersistence.core.domain.builders;


import br.com.copyimagem.mspersistence.core.domain.entities.*;
import br.com.copyimagem.mspersistence.core.domain.enums.FinancialSituation;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.copyimagem.mspersistence.core.domain.builders.AddressBuilder.oneAddress;
import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;


public class NaturalPersonCustomerBuilder implements Serializable {
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
    private String cpf;

    private NaturalPersonCustomerBuilder(){}

    public static NaturalPersonCustomerBuilder oneNaturalPersonCustomer() {
        NaturalPersonCustomerBuilder builder = new NaturalPersonCustomerBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    private static void initializeDefaultData(NaturalPersonCustomerBuilder builder) {
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
        builder.cpf = "156.258.240-29";
    }

    public NaturalPersonCustomerBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public NaturalPersonCustomerBuilder withClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public NaturalPersonCustomerBuilder withPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
        return this;
    }

    public NaturalPersonCustomerBuilder withEmailList(String... emailList) {
        this.emailList = Arrays.asList(emailList);
        return this;
    }

    public NaturalPersonCustomerBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public NaturalPersonCustomerBuilder withWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
        return this;
    }

    public NaturalPersonCustomerBuilder withBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public NaturalPersonCustomerBuilder withAddress(Address adress) {
        this.address = adress;
        return this;
    }

    public NaturalPersonCustomerBuilder withFinancialSituation(FinancialSituation financialSituation) {
        this.financialSituation = financialSituation;
        return this;
    }

    public NaturalPersonCustomerBuilder withPayDay(byte payDay) {
        this.payDay = payDay;
        return this;
    }

    public NaturalPersonCustomerBuilder withCustomerContractList(CustomerContract customerContract) {
        this.customerContract = customerContract;
        return this;
    }

    public NaturalPersonCustomerBuilder withMultiPrinterList(MultiPrinter... multiPrinterList) {
        this.multiPrinterList = Arrays.asList(multiPrinterList);
        return this;
    }

    public NaturalPersonCustomerBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public NaturalPersonCustomer nowCustomerPF() {
        NaturalPersonCustomer customer = new NaturalPersonCustomer(cpf);
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

    public NaturalPersonCustomerDTO nowCustomerPFDTO() {

        return new NaturalPersonCustomerDTO(
                id,
                clientName,
                cpf,
                primaryEmail,
                emailList,
                phoneNumber,
                whatsapp,
                bankCode,
                address,
                financialSituation.name(),
                payDay,
                customerContract,
                List.of( oneMultiPrinter().now() )
        );
    }
}
