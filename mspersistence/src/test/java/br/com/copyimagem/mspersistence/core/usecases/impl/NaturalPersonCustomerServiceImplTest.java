package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.DataIntegrityViolationException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.NaturalPersonCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder.oneNaturalPersonCustomer;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class NaturalPersonCustomerServiceImplTest {

    public static final long ID1L = 1L;

    public static final String CPF = "123.456.789-01";

    private NaturalPersonCustomer customerPf;

    private NaturalPersonCustomerDTO customerPfDTO;

    @Mock
    private NaturalPersonCustomerRepository naturalPersonCustomerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @InjectMocks
    private NaturalPersonCustomerServiceImpl naturalPersonCustomerService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        start();
    }

    @Test
    @DisplayName("Should return a list of NaturalPersonCustomer")
    void shouldReturnAListOfNaturalPersonCustomer(){
        String email = "carige@mail.com";
        when(naturalPersonCustomerRepository.findAll()).thenReturn( List.of(customerPf));
        when(convertObjectToObjectDTOService.convertToNaturalPersonCustomerDTO(customerPf)).thenReturn(customerPfDTO);
        List<NaturalPersonCustomerDTO> natural = naturalPersonCustomerService.findAllNaturalPersonCustomer();
        assertAll("NaturalPersonCustomer",
                () -> assertNotNull(natural),
                () -> assertEquals(1, natural.size()),
                () -> assertEquals(NaturalPersonCustomerDTO.class, natural.get(0).getClass()),
                () -> assertEquals(customerPfDTO, natural.get(0)),
                () -> assertEquals(email, natural.get(0).getPrimaryEmail()),
                () -> assertEquals(CPF, natural.get(0).getCpf())
        );

    }

    @Test
    @DisplayName("should return a NaturalPersonCustomerDTO by Id")
    void shouldReturnANaturalPersonCustomerDTOById(){
        when(naturalPersonCustomerRepository.findById(ID1L)).thenReturn( Optional.of(customerPf));

        when(convertObjectToObjectDTOService.convertToNaturalPersonCustomerDTO(customerPf)).thenReturn(customerPfDTO);
        NaturalPersonCustomerDTO expectedDTO = naturalPersonCustomerService.findNaturalPersonCustomerById(1L);

        assertAll("NaturalPersonCustomer",
                () -> assertNotNull(expectedDTO),
                () -> assertEquals(ID1L, expectedDTO.getId()),
                () -> assertEquals(expectedDTO, customerPfDTO),
                () -> assertEquals(NaturalPersonCustomerDTO.class, expectedDTO.getClass())
        );
    }

    @Test
    @DisplayName("Should return a empty when NaturalPersonCustomer not found")
    void shouldReturnEmptyWhenNaturalPersonCustomerNotFound(){
        when(naturalPersonCustomerRepository.findById(11L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> naturalPersonCustomerService
                .findNaturalPersonCustomerById(11L));
        verify(naturalPersonCustomerRepository, times(1)).findById(11L);
        try {
            naturalPersonCustomerService.findNaturalPersonCustomerById(11L);
        } catch (Exception ex) {
            assertEquals( NoSuchElementException.class, ex.getClass());
            assertEquals("Customer not found", ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should save a NaturalPersonCustomer")
    void shouldSaveANaturalPersonCustomer(){
        when(convertObjectToObjectDTOService.convertToNaturalPersonCustomer(customerPfDTO)).thenReturn(customerPf);
        when(customerRepository.existsCustomerByPrimaryEmail(customerPfDTO.getPrimaryEmail())).thenReturn(false);
        when(naturalPersonCustomerRepository
                                    .existsNaturalPersonCustomerByCpf(customerPfDTO.getCpf())).thenReturn(false);
        when(naturalPersonCustomerRepository.save(customerPf)).thenReturn(customerPf);
        when(convertObjectToObjectDTOService.convertToNaturalPersonCustomerDTO(customerPf)).thenReturn(customerPfDTO);
        when(addressRepository.save(customerPf.getAddress())).thenReturn(customerPfDTO.getAddress());
        NaturalPersonCustomerDTO natural = naturalPersonCustomerService.saveNaturalPersonCustomer(customerPfDTO);
        assertAll("NaturalPersonCustomerDTO",
                () -> assertNotNull(natural),
                () -> assertEquals(customerPfDTO, natural),
                () ->  assertEquals(customerPfDTO.getId(), natural.getId()),
                () ->  assertEquals(customerPfDTO.getCpf(), natural.getCpf()),
                () ->  assertEquals(customerPfDTO.getClass(), natural.getClass())
        );
        verify(convertObjectToObjectDTOService, times(1))
                                                                        .convertToNaturalPersonCustomer(customerPfDTO);
        verify(customerRepository, times(1))
                                                        .existsCustomerByPrimaryEmail(customerPfDTO.getPrimaryEmail());
        verify(naturalPersonCustomerRepository, times(1))
                                                             .existsNaturalPersonCustomerByCpf(customerPfDTO.getCpf());
        verify(naturalPersonCustomerRepository, times(1)).save(customerPf);
    }

    @Test
    @DisplayName("Should throw Exceptionn return when EMAIL already exists")
    void shouldReturnThrowExceptionWhenEmailAlreadyExists() {
        when(customerRepository.existsCustomerByPrimaryEmail(customerPfDTO.getPrimaryEmail()))
                .thenReturn(true);
        DataIntegrityViolationException dataException = assertThrows(DataIntegrityViolationException.class,() ->
                naturalPersonCustomerService.saveNaturalPersonCustomer(customerPfDTO));
        assertTrue(dataException.getMessage().startsWith("Email"));
    }

    @Test
    @DisplayName("Should throw return Exception when CPF already exists")
    void shouldReturnThrowExceptionWhenCpfAlreadyExists() {
        when(naturalPersonCustomerRepository.existsNaturalPersonCustomerByCpf(customerPfDTO.getCpf()))
                .thenReturn(true);
        String dataException = assertThrows(DataIntegrityViolationException.class,() ->
                naturalPersonCustomerService.saveNaturalPersonCustomer(customerPfDTO)).getMessage();
        assertTrue(dataException.startsWith("CPF"));
        assertEquals("CPF already exists!", dataException);
    }

    @Test
    void findByCpf() {

    }

    private void start() {

        customerPf = oneNaturalPersonCustomer().withId( ID1L ).withCpf( CPF ).nowCustomerPF();
        customerPfDTO = oneNaturalPersonCustomer().withId( ID1L ).withCpf( CPF ).nowCustomerPFDTO();
    }

}