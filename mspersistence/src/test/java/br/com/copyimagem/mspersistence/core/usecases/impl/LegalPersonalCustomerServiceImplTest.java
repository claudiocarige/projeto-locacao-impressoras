package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
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

import java.util.List;
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

        LegalPersonalCustomerDTO legalPersonalCustomerDTO =
                                                     legalPersonalCustomerService.findLegalPersonalCustomerById(ID1L);

        assertAll("LegalPersonalCustomer",
                () -> assertNotNull(legalPersonalCustomerDTO),
                () -> assertEquals(ID1L, legalPersonalCustomerDTO.getId()),
                () -> assertEquals(legalPersonalCustomerDTO, customerPjDTO),
                () -> assertEquals(LegalPersonalCustomerDTO.class, legalPersonalCustomerDTO.getClass())
        );
    }

    @Test
    @DisplayName("shoul return a empty when LegalPersonalCustomer not found")
    void shoulReturnEmptyWhenNaturalPersonCustomerNotFound() {
        when(legalPersonalCustomerRepository.findById(ID1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> legalPersonalCustomerService.findLegalPersonalCustomerById(ID1L));
        try {
            legalPersonalCustomerService.findLegalPersonalCustomerById(ID1L);
        } catch (NoSuchElementException message) {
            assertEquals("Customer not found", message.getMessage());
            assertEquals( NoSuchElementException.class, message.getClass());
        }
    }

    @Test
    @DisplayName("should return a list of legalPersonalCustomer")
    void shouldReturnAListLegalPersonalCustomer() {
        when(legalPersonalCustomerRepository.findAll()).thenReturn( List.of(customerPj));
        when(convertObjectToObjectDTOService.convertToLegalPersonalCustomerDTO(customerPj)).thenReturn(customerPjDTO);

        List<LegalPersonalCustomerDTO> legalPersonalCustomerList =
                                                          legalPersonalCustomerService.findAllLegalPersonalCustomer();
        assertAll("LegalPersonalCustomerLis",
                () -> assertNotNull(legalPersonalCustomerList),
                () -> assertEquals(1, legalPersonalCustomerList.size()),
                () -> assertEquals(LegalPersonalCustomerDTO.class, legalPersonalCustomerList.get(0).getClass()),
                () -> {
                    assertAll("MultPrint",
                            () -> assertNotNull(legalPersonalCustomerList.get(0).getMultiPrinterList()),
                            () -> assertEquals( MultiPrinterDTO.class, legalPersonalCustomerList.get(0)
                                                                           .getMultiPrinterList().get(0).getClass()),
                            () -> assertEquals(1, legalPersonalCustomerList.get(0)
                                                                                      .getMultiPrinterList().size())
                    );
                },
                () -> {
                    assertAll("MonthlyPayment",
                            () -> assertEquals(1, legalPersonalCustomerList.get(0)
                                                                                    .getMonthlyPaymentList().size()),
                            () -> assertEquals( MonthlyPayment.class, legalPersonalCustomerList.get(0)
                                                                         .getMonthlyPaymentList().get(0).getClass())
                    );
                }
        );

    }



    private void start() {

        customerPj = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJ();
        customerPjDTO = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJDTO();

    }
}