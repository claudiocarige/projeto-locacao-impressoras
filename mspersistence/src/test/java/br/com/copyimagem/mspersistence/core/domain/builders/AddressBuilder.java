package br.com.copyimagem.mspersistence.core.domain.builders;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;


public class AddressBuilder {
    private Long id;
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;

    private AddressBuilder(){}

    public static AddressBuilder oneAddress() {
        AddressBuilder builder = new AddressBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    private static void initializeDefaultData(AddressBuilder builder) {
        builder.id = 1L;
        builder.street = "Rua Estevam Barbosa Alves";
        builder.number = "12";
        builder.city = "Salvador";
        builder.state = "Bahia";
        builder.country = "Brasil";
    }

    public AddressBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AddressBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public AddressBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public AddressBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public Address now() {
        return new Address(id, street, number, city, state, country);
    }
}