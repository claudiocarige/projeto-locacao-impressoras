package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


class CustomerServiceImplTest {
    public static final long ID1L = 1L;
    public static final String CNPJ = "14.124.420/0001-94";
    public static final String CPF = "156.258.240-29";
    public static final String EMAIL = "claudio@mail.com.br";
    private Customer customer;
    private LegalPersonalCustomer legalPersonalCustomer;
    private NaturalPersonCustomer naturalPersonCustomer;
    private CustomerResponseDTO customerResponseDTOPJ;
    private CustomerResponseDTO customerResponseDTOPF;
    private UpdateCustomerDTO updateCustomerDTOPJ;
    private UpdateCustomerDTO updateCustomerDTOPF;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;
    @Mock
    private LegalPersonalCustomerServiceImpl legalPersonalCustomerService;
    @Mock
    private NaturalPersonCustomerServiceImpl naturalPersonCustomerService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        start();
    }

}