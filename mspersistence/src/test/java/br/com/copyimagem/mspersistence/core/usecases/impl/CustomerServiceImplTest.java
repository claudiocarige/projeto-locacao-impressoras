package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.builders.CustomerResponseDTOBuilder;
import br.com.copyimagem.mspersistence.core.domain.builders.LegalPersonalCustomerBuilder;
import br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder;
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


    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        start();
    }




    private void start() {

        customer = LegalPersonalCustomerBuilder.oneLegalPersonalCustomer().nowCustomerPJ();
        legalPersonalCustomer = LegalPersonalCustomerBuilder.oneLegalPersonalCustomer().nowCustomerPJ();
        naturalPersonCustomer = NaturalPersonCustomerBuilder.oneNaturalPersonCustomer().nowCustomerPF();
        customerResponseDTOPJ = CustomerResponseDTOBuilder.oneCustomerResponseDTO().withCpfOrCnpj( CNPJ ).now();
        customerResponseDTOPF = CustomerResponseDTOBuilder.oneCustomerResponseDTO().withCpfOrCnpj( CPF ).now();
        updateCustomerDTOPJ = new UpdateCustomerDTO();
        updateCustomerDTOPJ.setPrimaryEmail( EMAIL );
        updateCustomerDTOPJ.setId( ID1L );
        updateCustomerDTOPJ.setCpfOrCnpj( CNPJ );
        updateCustomerDTOPJ.setClientName( "Claudio Carigé" );
        updateCustomerDTOPJ.setPhoneNumber( "7132104567" );
        updateCustomerDTOPJ.setWhatsapp( "71998987878" );
        updateCustomerDTOPJ.setBankCode( "123" );
        updateCustomerDTOPJ.setPayDay( Byte.parseByte( "5" ) );
        updateCustomerDTOPJ.setFinancialSituation( "PAGO" );
        updateCustomerDTOPF = new UpdateCustomerDTO();
        updateCustomerDTOPF.setPrimaryEmail( EMAIL );
        updateCustomerDTOPF.setId( 2L );
        updateCustomerDTOPF.setCpfOrCnpj( CPF );
    }

}