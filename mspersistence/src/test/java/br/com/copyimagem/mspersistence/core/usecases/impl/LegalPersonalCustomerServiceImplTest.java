package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.DataIntegrityViolationException;
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
import static org.mockito.Mockito.*;


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

    @Test
    @DisplayName("should save a LegalPersonalCustomer")
    void shouldSaveALegalPersonalCustomer() {
        when(customerRepository.existsCustomerByPrimaryEmail(customerPjDTO.getPrimaryEmail()))
                .thenReturn(false);
        when(legalPersonalCustomerRepository.existsLegalPersonalCustomerByCnpj(CNPJ)).thenReturn(false);
        when(legalPersonalCustomerRepository.save(customerPj)).thenReturn(customerPj);
        when(convertObjectToObjectDTOService.convertToLegalPersonalCustomerDTO(customerPj)).thenReturn(customerPjDTO);
        when(convertObjectToObjectDTOService.convertToLegalPersonalCustomer(customerPjDTO)).thenReturn(customerPj);
        when(addressRepository.save(customerPj.getAddress())).thenReturn(customerPjDTO.getAddress());
        LegalPersonalCustomerDTO legalPersonalCustomerDTO = legalPersonalCustomerService
                .saveLegalPersonalCustomer(customerPjDTO);
        assertAll("LegalPersonalCustomer",
                () -> assertNotNull(legalPersonalCustomerDTO),
                () -> assertEquals(customerPjDTO.getId(), legalPersonalCustomerDTO.getId()),
                () -> assertEquals(LegalPersonalCustomerDTO.class, legalPersonalCustomerDTO.getClass()),
                () -> assertEquals(customerPjDTO, legalPersonalCustomerDTO),
                () -> assertEquals(customerPjDTO.getPrimaryEmail(), legalPersonalCustomerDTO.getPrimaryEmail()),
                () -> assertEquals(customerPjDTO.getClass(), legalPersonalCustomerDTO.getClass()),
                () -> {
                    assertAll("MultPrint",
                            () -> assertNotNull(legalPersonalCustomerDTO.getMultiPrinterList()),
                            () -> assertEquals(MultiPrinterDTO.class, legalPersonalCustomerDTO.getMultiPrinterList().get(0).getClass()),
                            () -> assertEquals(1, legalPersonalCustomerDTO.getMultiPrinterList().size())
                    );
                },
                () -> {
                    assertAll("MonthlyPayment",
                            () -> assertEquals(1, legalPersonalCustomerDTO.getMonthlyPaymentList().size()),
                            () -> assertEquals(MonthlyPayment.class, legalPersonalCustomerDTO.getMonthlyPaymentList().get(0).getClass())
                    );
                }
        );
        verify(convertObjectToObjectDTOService, times(1))
                .convertToLegalPersonalCustomerDTO(customerPj);
        verify(convertObjectToObjectDTOService, times(1))
                .convertToLegalPersonalCustomer(customerPjDTO);
        verify(customerRepository, times(1))
                .existsCustomerByPrimaryEmail(customerPjDTO.getPrimaryEmail());
        verify(legalPersonalCustomerRepository, times(1)).existsLegalPersonalCustomerByCnpj(customerPj.getCnpj());
    }

    @Test
    @DisplayName("Must return exception when Address is null")
    void mustReturnExceptionWhenAddressIsNull(){
        customerPjDTO.setAddress(null);
        String message = assertThrows( DataIntegrityViolationException.class,
                () -> legalPersonalCustomerService.saveLegalPersonalCustomer(customerPjDTO)).getMessage();
        assertEquals("Address is null!", message);
    }

    @Test
    @DisplayName("Must return exception when Email exist")
    void mustReturnExceptionWhenEmailExist(){
        when(customerRepository.existsCustomerByPrimaryEmail(customerPjDTO.getPrimaryEmail()))
                .thenReturn(true);
        String message = assertThrows(DataIntegrityViolationException.class,
                () -> legalPersonalCustomerService.saveLegalPersonalCustomer(customerPjDTO)).getMessage();
        assertEquals("Email already exists!", message);
    }

    @Test
    @DisplayName("Must return exception when Cnpj exist")
    void mustReturnExceptionWhenCnpjExist(){
        when(legalPersonalCustomerRepository.existsLegalPersonalCustomerByCnpj(customerPjDTO.getCnpj()))
                .thenReturn(true);
        String message = assertThrows(DataIntegrityViolationException.class,
                () -> legalPersonalCustomerService.saveLegalPersonalCustomer(customerPjDTO)).getMessage();
        assertEquals("CNPJ already exists!", message);
    }

    @Test
    @DisplayName("Must retunr a Customer Response DTO By CNPJ")
    void mustReturnACustomerResponseDTOByCpnj(){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(ID1L);
        customerResponseDTO.setCpfOrCnpj(CNPJ);
        customerResponseDTO.setClientName(customerPj.getClientName());
        customerResponseDTO.setAddress(customerPj.getAddress());
        when(legalPersonalCustomerRepository.findByCnpj(CNPJ)).thenReturn(Optional.of(customerPj));
        when(convertObjectToObjectDTOService.convertToCustomerResponseDTO(customerPj)).thenReturn(customerResponseDTO);
        CustomerResponseDTO responseDTO = legalPersonalCustomerService.findByCnpj(CNPJ);
        assertAll("CustomerResponseDTO",
                () -> assertNotNull(responseDTO),
                () -> assertEquals(customerResponseDTO, responseDTO),
                () -> assertEquals(CustomerResponseDTO.class, responseDTO.getClass()),
                () -> assertEquals(ID1L, responseDTO.getId()),
                () -> assertEquals(CNPJ, responseDTO.getCpfOrCnpj()),
                () -> assertEquals(customerResponseDTO.getClientName(), responseDTO.getClientName()),
                () -> assertEquals(customerResponseDTO.getAddress(), responseDTO.getAddress())
        );
    }

    @Test
    @DisplayName("must return a empty when CNPJ not found")
    void mustReturnEmptyWhenCnpjNotFound(){
        when(legalPersonalCustomerRepository.findByCnpj(CNPJ)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> legalPersonalCustomerService.findByCnpj(CNPJ));
        try {
            legalPersonalCustomerService.findByCnpj(CNPJ);
        } catch (NoSuchElementException message) {
            assertEquals("Customer not found", message.getMessage());
            assertEquals(NoSuchElementException.class, message.getClass());
        }
    }

    private void start() {

        customerPj = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJ();
        customerPjDTO = oneLegalPersonalCustomer()
                .withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJDTO();

    }
}