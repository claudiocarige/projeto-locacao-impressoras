package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.builders.CustomerResponseDTOBuilder;
import br.com.copyimagem.mspersistence.core.domain.builders.LegalPersonalCustomerBuilder;
import br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder;
import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

        MockitoAnnotations.openMocks( this );
        start();
    }

    @Test
    @DisplayName( "Should return a Customer by ID " )
    void shouldReturnACustomerByID() {

        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.of( legalPersonalCustomer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( legalPersonalCustomer ) )
                                                                                  .thenReturn( customerResponseDTOPJ );
        CustomerResponseDTO customerResponseDTO = customerService.searchCustomer( "id", "1" );
        assertEquals( customerResponseDTOPJ, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTOPJ.getClass() );
        assertEquals( customerResponseDTO.getCpfOrCnpj(), customerResponseDTOPJ.getCpfOrCnpj() );
    }

    @Test
    @DisplayName( "Should return a empty when Customer not found by ID" )
    void shouldReturnAEmptyWhenCustomerNotFoundById() {

        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.empty() );
        String message = assertThrows( NoSuchElementException.class,
                () -> customerService.searchCustomer( "id", "1" ) ).getMessage();
        assertEquals( "Customer not found", message );

    }

    @Test
    @DisplayName( "Should return a Customer by CPF" )
    void shouldReturnACustomerByCPF() {

        when( naturalPersonCustomerService.findByCpf( CPF ) ).thenReturn( customerResponseDTOPF );
        CustomerResponseDTO customerResponseDTO = customerService.searchCustomer( "cpf", CPF );
        assertEquals( customerResponseDTOPF, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTOPF.getClass() );
        assertEquals( customerResponseDTO.getCpfOrCnpj(), customerResponseDTOPF.getCpfOrCnpj() );
    }

    @Test
    @DisplayName( "Should return a empty when Customer not found by CPF" )
    void shouldReturnAEmptyWhenCustomerNotFoundByCPF() {

        when( naturalPersonCustomerService.findByCpf( CPF ) ).thenReturn( null );
        try {
            customerService.searchCustomer( "cpf", CPF );
        } catch( NoSuchElementException ex ) {
            assertEquals( "Customer not found", ex.getMessage() );
            assertEquals( NoSuchElementException.class, ex.getClass() );
        }
    }

    @Test
    @DisplayName( "Should return a Customer by CNPJ" )
    void shouldReturnACustomerByCNPJ() {

        when( legalPersonalCustomerService.findByCnpj( CNPJ ) ).thenReturn( customerResponseDTOPJ );
        CustomerResponseDTO customerResponseDTO = customerService.searchCustomer( "cnpj", CNPJ );
        assertEquals( customerResponseDTOPJ, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTO.getClass() );
        assertEquals( customerResponseDTOPJ.getCpfOrCnpj(), customerResponseDTO.getCpfOrCnpj() );
    }

    @Test
    @DisplayName( "Should return a empty when Customer not found by CNPJ" )
    void shouldReturnAEmptyWhenCustomerNotFounfByCNPJ() {

        when( legalPersonalCustomerService.findByCnpj( CNPJ ) ).thenReturn( null );
        try {
            customerService.searchCustomer( "cnpj", CNPJ );
        } catch( NoSuchElementException ex ) {
            assertEquals( "Customer not found", ex.getMessage() );
            assertEquals( NoSuchElementException.class, ex.getClass() );
        }
    }

    @Test
    @DisplayName( "Should return a Customer by Email" )
    void shouldReturnACustomerByEmail() {

        when( customerRepository.findByPrimaryEmail( EMAIL ) ).thenReturn( Optional.of( naturalPersonCustomer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( naturalPersonCustomer ) )
                                                                                  .thenReturn( customerResponseDTOPF );
        CustomerResponseDTO customerResponseDTO = customerService.searchCustomer( "email", EMAIL );
        assertEquals( customerResponseDTOPF, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTOPF.getClass() );
        assertEquals( customerResponseDTO.getCpfOrCnpj(), customerResponseDTOPF.getCpfOrCnpj() );
    }

    @Test
    @DisplayName( "Should return a not found Customer by Email" )
    void shouldReturnAEmptyWhenCustomerNotFoundByEmail() {

        when( customerRepository.findByPrimaryEmail( EMAIL ) ).thenReturn( Optional.empty() );
        String message = assertThrows( NoSuchElementException.class,
                () -> customerService.searchCustomer( "email", EMAIL ) ).getMessage();
        assertEquals( "Customer not found", message );

    }

    @Test
    @DisplayName( "Should return a Customer by clientName" )
    void shouldReturnACustomerByClientName() {

        when( customerRepository.findByClientName( customer.getClientName() ) ).thenReturn( Optional.of( customer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( customer ) )
                                                                                 .thenReturn( customerResponseDTOPJ );
        CustomerResponseDTO customerResponseDTO = customerService
                                                .searchCustomer( "clientName", "Claudio Carigé" );
        assertEquals( customerResponseDTOPJ, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTO.getClass() );
        assertEquals( customer.getClientName(), customerResponseDTO.getClientName() );
    }

    @Test
    @DisplayName( "Should return a not Found Customer by clientName" )
    void shouldReturnANotFoundCustomerByClientName() {

        when( customerRepository.findByClientName( customer.getClientName() ) ).thenReturn( Optional.empty() );
        var message = assertThrows( NoSuchElementException.class, () ->
                customerService.searchCustomer( "clientName", "Claudio Carigé" ) ).getMessage();
        assertEquals( "Customer not found", message );

    }

    @Test
    @DisplayName( "Should return a Customer by PhoneNumber" )
    void shouldReturnACustomerByPhoneNumber() {

        when( customerRepository.findByPhoneNumber( customer.getPhoneNumber() ) ).thenReturn( Optional.of( customer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( customer ) )
                                                                                  .thenReturn( customerResponseDTOPJ );
        CustomerResponseDTO customerResponseDTO = customerService
                .searchCustomer( "phoneNumber", "7132104567" );
        assertEquals( customerResponseDTOPJ, customerResponseDTO );
        assertEquals( CustomerResponseDTO.class, customerResponseDTO.getClass() );
        assertEquals( customer.getPhoneNumber(), customerResponseDTO.getPhoneNumber() );
    }

    @Test
    @DisplayName( "Should return a Exception when not found Customer by PhoneNumber" )
    void shouldReturnAExceptionWhenNotFoundCustomerByPhoneNumber() {

        when( customerRepository.findByPhoneNumber( customer.getPhoneNumber() ) ).thenReturn( Optional.empty() );
        var message = assertThrows( NoSuchElementException.class,
                () -> customerService.searchCustomer( "phoneNumber", "7132100000" ) ).getMessage();
        assertEquals( "Customer not found", message );
    }

    @Test
    @DisplayName( "Should return a CustomerContract with sucess." )
    void shouldReturnACustomerContractWithSucess() {

        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.of( legalPersonalCustomer ) );
        CustomerContract customerContract = customerService.getCustomerContract( ID1L );
        assertEquals( CustomerContract.class, customerContract.getClass() );
        assertEquals( legalPersonalCustomer.getCustomerContract().getId(), customerContract.getId() );
    }

    @Test
    @DisplayName( "Should return a not found CustomerContract" )
    void shouldReturnANotFoundCustomerContract() {

        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.empty() );
        var message = assertThrows( NoSuchElementException.class, () ->
                customerService.getCustomerContract( ID1L ) ).getMessage();

        assertEquals( "Customer not found", message );
    }

    @Test
    @DisplayName( "Should return exception when typeParam is not accepted" )
    void shouldReturnExceptionWhenTypeParamIsNotAccepted() {

        String typeParam = "teste";
        String message = assertThrows( IllegalArgumentException.class,
                () -> customerService.searchCustomer( typeParam, "teste" ) ).getMessage();
        assertEquals( "Parameter [ " + typeParam + " ] type not accepted.", message );
    }

    @Test
    @DisplayName( "Should return all customers" )
    void shouldReturnAllCustomers() {

        when( customerRepository.findAll() ).thenReturn( Collections.singletonList( legalPersonalCustomer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( legalPersonalCustomer ) )
                                                                                  .thenReturn( customerResponseDTOPJ );
        List< CustomerResponseDTO > customerResponseDTO = customerService.searchAllCustomers();
        assertEquals( 1, customerResponseDTO.size() );
        assertEquals( legalPersonalCustomer.getId(), customerResponseDTO.get( 0 ).getId() );
        assertEquals( CustomerResponseDTO.class, customerResponseDTO.get( 0 ).getClass() );
    }

    @Test
    @DisplayName( "Should return all customers by FinancialSituation" )
    void shouldReturnAllCustomersByFinancialSituation() {

        String situation = "PAGO";
        when( customerRepository.findAllByFinancialSituation( any() ) ).
                thenReturn( Collections.singletonList( legalPersonalCustomer ) );
        when( convertObjectToObjectDTOService.convertToCustomerResponseDTO( legalPersonalCustomer ) )
                .thenReturn( customerResponseDTOPJ );
        List< CustomerResponseDTO > customerResponseDTO = customerService.searchFinancialSituation( situation );
        assertEquals( 1, customerResponseDTO.size() );
        assertEquals( legalPersonalCustomer.getId(), customerResponseDTO.get( 0 ).getId() );
        assertEquals( CustomerResponseDTO.class, customerResponseDTO.get( 0 ).getClass() );
        assertEquals( situation, customerResponseDTO.get( 0 ).getFinancialSituation() );
    }

    @ParameterizedTest
    @CsvSource( value = {
            "cpf, 156.258.240-29, 2",
            "cnpj, 14.124.420/0001-94, 1",
            "primaryEmail, claudio@mail.com.br, 1",
            "clientName, Claudio Carigé, 1",
            "phoneNumber, 7132104567, 1",
            "whatsapp, 71998987878, 1",
            "bankCode, 123, 1",
            "payDay, 5, 1",
            "financialSituation, PAGO, 1",
    } )
    @DisplayName( "Should update the Customer by attribute" )
    void shouldUpdateTheCustomerByAttribute( String attribute, String val, String id ) {

        when( customerRepository.findById( 1L ) ).thenReturn( Optional.of( legalPersonalCustomer ) );
        when( customerRepository.findById( 2L ) ).thenReturn( Optional.of( naturalPersonCustomer ) );
        when( customerRepository.save( legalPersonalCustomer ) ).thenReturn( legalPersonalCustomer );
        when( customerRepository.save( naturalPersonCustomer ) ).thenReturn( naturalPersonCustomer );
        when( convertObjectToObjectDTOService.convertToUpdateCustomerDTO( legalPersonalCustomer ) )
                                                                                   .thenReturn( updateCustomerDTOPJ );
        when( convertObjectToObjectDTOService.convertToUpdateCustomerDTO( naturalPersonCustomer ) )
                                                                                   .thenReturn( updateCustomerDTOPF );
        UpdateCustomerDTO updateCustomerResultDTO = customerService
                                                     .updateCustomerAttribute( attribute, val, Long.parseLong( id ) );

        assertNotNull( updateCustomerResultDTO );
        assertEquals( UpdateCustomerDTO.class, updateCustomerResultDTO.getClass() );

        switch( attribute ) {
            case "cnpj" -> assertEquals( val, updateCustomerDTOPJ.getCpfOrCnpj() );
            case "cpf" -> assertEquals( val, updateCustomerDTOPF.getCpfOrCnpj() );
            case "primaryEmail" -> assertEquals( val, updateCustomerDTOPJ.getPrimaryEmail() );
            case "clienteName" -> assertEquals( val, updateCustomerDTOPJ.getClientName() );
            case "phoneNumber" -> assertEquals( val, updateCustomerDTOPJ.getPhoneNumber() );
            case "whatsapp" -> assertEquals( val, updateCustomerDTOPJ.getWhatsapp() );
            case "bankCode" -> assertEquals( val, updateCustomerDTOPJ.getBankCode() );
            case "payDay" -> assertEquals( Byte.parseByte( val ), updateCustomerDTOPJ.getPayDay() );
            case "financialSituation" -> assertEquals( val, updateCustomerDTOPJ.getFinancialSituation() );
        }
    }

    @Test
    @DisplayName( "Should return  a exception when the attribute is null" )
    void shouldReturnAExceptionWhenTheAttributeIsNull() {

        String attribute = "clientName";
        String message = assertThrows( IllegalArgumentException.class,
                () -> customerService.updateCustomerAttribute( attribute, null, ID1L ) ).getMessage();
        assertEquals( attribute.toUpperCase() + " cannot be null.", message );
    }

    @Test
    @DisplayName( "Should return a exception when the attribute is in the list" )
    void shouldReturnAExceptionWhenTheAttributeIsInTheList() {

        String attribute = "emailList";
        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.of( customer ) );
        String message = assertThrows( IllegalArgumentException.class, () -> customerService
                                    .updateCustomerAttribute( attribute, "mail@mail.com", ID1L ) ).getMessage();
        assertEquals( "This attribute cannot be changed on this endpoint.", message );
    }

    @Test
    @DisplayName( "Should return a exception when attribute not found" )
    void shouldReturnAExceptionWhenAttributeNotFound() {

        String attribute = "homeNumber";
        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.of( customer ) );

        String message = assertThrows( IllegalArgumentException.class,
                () -> customerService.updateCustomerAttribute( attribute, "5", ID1L ) ).getMessage();
        assertEquals( "Attribute not found.", message );
    }

    @ParameterizedTest
    @CsvSource( value = {
            "primaryEmail,Email format is invalid,ccarige.mail ",
            "cnpj, CNPJ format is invalid, il.123.com/1234-br",
            "cpf, CPF format is invalid, 894.965.31-02"} )
    @DisplayName( "Should return a exception when Attributes are invalid" )
    void shouldReturnAExceptionWhenAttributesAreInvalid( String attribute, String messages, String val ) {

        when( customerRepository.findById( ID1L ) ).thenReturn( Optional.of( customer ) );
        String message = assertThrows( IllegalArgumentException.class,
                () -> customerService.updateCustomerAttribute( attribute, val, ID1L ) ).getMessage();
        assertEquals( messages, message );
        verify( customerRepository, times( 1 ) ).findById( ID1L );
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