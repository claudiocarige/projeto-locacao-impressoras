package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Objects;

import static br.com.copyimagem.mspersistence.core.domain.builders.CustomerResponseDTOBuilder.oneCustomerResponseDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


class CustomerControllerImplTest {

    public static final String CUSTOMER_NOT_FOUND = "Customer not found";

    public static final String NUMBER_1 = "1";

    public static final String CPF = "156.258.240-29";

    public static final String ID_TYPE_PARAM = "id";

    public static final String CPF_OR_CNPJ = "14.124.420/0001-94";

    public static final String CNPJ_PARAM = "cnpj";

    public static final String CCARIGE_GMAIL_COM = "carige@mail.com";

    private CustomerResponseDTO customerResponseDTO;

    private UpdateCustomerDTO updateCustomerDTO;

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( customerController ).build();
        start();
    }

    @Test
    @DisplayName( "Should return a customer by id" )
    void shouldReturnACustomerById() throws Exception {

        when( customerService.searchCustomer( ID_TYPE_PARAM, NUMBER_1 ) ).thenReturn( customerResponseDTO );
        mockMvc.perform( get( "/api/v1/customers/search-client" )
                        .param( "typeParam", ID_TYPE_PARAM )
                        .param( "valueParam", NUMBER_1 )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( 1L ) );
    }

    @Test
    @DisplayName( "Should return a exception when customer not found." )
    void shouldReturnAExceptionWhenCustomerNotFound() {

        when( customerService.searchCustomer( ID_TYPE_PARAM, NUMBER_1 ) )
                                                     .thenThrow( new NoSuchElementException( "Customer not found" ) );
        NoSuchElementException exception = assertThrows( NoSuchElementException.class,
                                                    () -> customerService.searchCustomer( ID_TYPE_PARAM, NUMBER_1 ) );
        assertEquals( CUSTOMER_NOT_FOUND, exception.getMessage() );

        verify( customerService, Mockito.times( 1 ) ).searchCustomer( ID_TYPE_PARAM, NUMBER_1 );
    }

    @Test
    @DisplayName( "Should return all customers" )
    void shouldReturnAllCustomers() throws Exception {

        when( customerService.searchAllCustomers() ).thenReturn( List.of( customerResponseDTO ) );

        ResponseEntity< List< CustomerResponseDTO > > responseEntity = customerController.searchAllCustomers();
        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( CustomerResponseDTO.class, Objects.requireNonNull( responseEntity.getBody() )
                                                                                              .get( 0 ).getClass() );
        assertEquals( 1, responseEntity.getBody().size() );

        mockMvc.perform( get( "/api/v1/customers/search-client-all" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$[0].id" ).value( 1 ) )
                .andExpect( jsonPath( "$[0].primaryEmail" ).value( CCARIGE_GMAIL_COM ) )
                .andExpect( jsonPath( "$[0].cpfOrCnpj" ).value( CPF ) );
    }

    @Test
    @DisplayName( "Should return all customers by FinancialSituation" )
    void shouldReturnAllCustomersByFinancialSituation() throws Exception {

        String situation = "PAGO";
        when( customerService.searchFinancialSituation( situation ) ).thenReturn( List.of( customerResponseDTO ) );
        ResponseEntity< List< CustomerResponseDTO > > responseEntity =
                                                             customerController.searchFinancialSituation( situation );
        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( CustomerResponseDTO.class,
                                             Objects.requireNonNull( responseEntity.getBody() ).get( 0 ).getClass() );
        assertEquals( 1, responseEntity.getBody().size() );

        mockMvc.perform( get( "/api/v1/customers/search-financial-situation" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .param( "situation", situation ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
    }

    @Test
    @DisplayName( "Should return a exception when param invalid" )
    void shouldReturnAExceptionWhenParamInvalidByFinancialSituation() {

        String situation = "INVALID";
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                                                     () -> customerController.searchFinancialSituation( situation ) );
        assertEquals( "The argument is not correct", exception.getMessage() );
        verify( customerService, never() ).searchFinancialSituation( situation );
    }

    @Test
    @DisplayName( "Should return a ResponseEntity from UpdateCustomerDTO" )
    void shouldReturnAResponseEntityFromUpdateCustomerDTO() throws Exception {

        when( customerService.updateCustomerAttribute( CNPJ_PARAM, CPF_OR_CNPJ, 1L ) ).thenReturn( updateCustomerDTO );
        ResponseEntity< UpdateCustomerDTO > responseEntity =
                customerController.updateCustomerAttribute( CNPJ_PARAM, CPF_OR_CNPJ, 1L );
        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( UpdateCustomerDTO.class, Objects.requireNonNull( responseEntity.getBody() ).getClass() );
        assertEquals( customerResponseDTO.getId(), responseEntity.getBody().getId() );

        mockMvc.perform( patch( "/api/v1/customers/1" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .param( "attribute", CNPJ_PARAM )
                        .param( "value", CPF_OR_CNPJ ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$.id" ).value( 1L ) )
                .andExpect( jsonPath( "$.primaryEmail" ).value( CCARIGE_GMAIL_COM ) );
    }

    private void start() {

        customerResponseDTO = oneCustomerResponseDTO().withCpfOrCnpj( CPF ).now();
        updateCustomerDTO = new UpdateCustomerDTO();
        updateCustomerDTO.setPrimaryEmail( CCARIGE_GMAIL_COM );
        updateCustomerDTO.setId( 1L );
        updateCustomerDTO.setCpfOrCnpj( CPF_OR_CNPJ );
    }

}