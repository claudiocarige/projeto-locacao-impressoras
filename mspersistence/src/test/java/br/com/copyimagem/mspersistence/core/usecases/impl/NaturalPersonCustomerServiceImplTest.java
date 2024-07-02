package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
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
import static org.mockito.Mockito.when;


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
    void saveNaturalPersonCustomer() {

    }

    @Test
    void findByCpf() {

    }

    private void start() {

        customerPf = oneNaturalPersonCustomer().withId( ID1L ).withCpf( CPF ).nowCustomerPF();
        customerPfDTO = oneNaturalPersonCustomer().withId( ID1L ).withCpf( CPF ).nowCustomerPFDTO();
    }

}