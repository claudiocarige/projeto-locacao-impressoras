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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


class CustomerControllerTest {

    public static final String CUSTOMER_NOT_FOUND = "Customer not found";

    public static final String NUMBER_1 = "1";

    public static final String CPF = "156.258.240-29";

    public static final String CPF_PARAM = "cpf";

    public static final String ID_TYPE_PARAM = "id";

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

        when( customerService.searchCustomer( ID_TYPE_PARAM, NUMBER_1 ) ).thenReturn(
                customerResponseDTO );
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
    @DisplayName("Should return all customers")
    void shouldReturnAllCustomers() throws Exception {
        when(customerService.searchAllCustomers()).thenReturn( List.of(customerResponseDTO));

        ResponseEntity<List<CustomerResponseDTO>> responseEntity =
                customerController.searchAllCustomers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(CustomerResponseDTO.class, Objects.requireNonNull(responseEntity.getBody()).get(0).getClass());
        assertEquals(1, responseEntity.getBody().size());

        mockMvc.perform(get("/api/v1/customers/search-client-all")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].primaryEmail").value("carige@mail.com"))
            .andExpect(jsonPath("$[0].cpfOrCnpj").value(CPF));
    }

    private void start() {

        customerResponseDTO = oneCustomerResponseDTO().withCpfOrCnpj( CPF ).now();
        updateCustomerDTO = new UpdateCustomerDTO();
        updateCustomerDTO.setPrimaryEmail( "ccarige@gmail.com" );
        updateCustomerDTO.setId( 1L );
        updateCustomerDTO.setCpfOrCnpj( "14.124.420/0001-94" );
    }

}