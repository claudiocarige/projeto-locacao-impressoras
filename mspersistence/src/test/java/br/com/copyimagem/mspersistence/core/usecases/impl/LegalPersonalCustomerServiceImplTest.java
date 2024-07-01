package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerContractRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.LegalPersonalCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.com.copyimagem.mspersistence.core.domain.builders.LegalPersonalCustomerBuilder.oneLegalPersonalCustomer;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class LegalPersonalCustomerServiceImplTest {

    public static final long ID1L = 1L;
    public static final String CNPJ = "12.345.678/0001-01";

    private LegalPersonalCustomer customerPj;

    private LegalPersonalCustomerDTO customerPjDTO;
    @Mock
    private LegalPersonalCustomerRepository legalPersonalCustomerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerContractRepository customerContractRepository;

    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private LegalPersonalCustomerServiceImpl legalPersonalCustomerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        start();
    }

    @Test
    @DisplayName("Must return a LegalPersonalCustomerDTO by Id")
    void shoulReturnALegalPersonalCustomerDTOById() {
        when(legalPersonalCustomerRepository.findById(ID1L)).thenReturn( Optional.of(customerPj));
        when(convertObjectToObjectDTOService.convertToLegalPersonalCustomerDTO(customerPj)).thenReturn(customerPjDTO);

        LegalPersonalCustomerDTO legalPersonalCustomerDTO = legalPersonalCustomerService.findLegalPersonalCustomerById(ID1L);

        assertAll("LegalPersonalCustomer",
                () -> assertNotNull(legalPersonalCustomerDTO),
                () -> assertEquals(ID1L, legalPersonalCustomerDTO.getId()),
                () -> assertEquals(legalPersonalCustomerDTO, customerPjDTO),
                () -> assertEquals(LegalPersonalCustomerDTO.class, legalPersonalCustomerDTO.getClass())
        );
    }


    private void start() {

        customerPj = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJ();
        customerPjDTO = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJDTO();

    }
}