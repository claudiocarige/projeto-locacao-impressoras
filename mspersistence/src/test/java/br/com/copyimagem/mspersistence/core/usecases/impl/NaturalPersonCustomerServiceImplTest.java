package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.NaturalPersonCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder.oneNaturalPersonCustomer;


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
    void findAllNaturalPersonCustomer() {

    }

    @Test
    void findNaturalPersonCustomerById() {

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