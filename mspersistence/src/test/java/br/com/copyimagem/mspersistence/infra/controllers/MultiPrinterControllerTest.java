package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Objects;

import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class MultiPrinterControllerTest {


    private MultiPrinterDTO multiPrinterDTO;

    private MockMvc mockMvc;

    @Mock
    private MultiPrinterService multiPrinterService;

    @InjectMocks
    private MultiPrinterController multiPrinterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( multiPrinterController ).build();
        start();

    }

    @Test
    @DisplayName( "Should return a list of MultiPrinter" )
    void shouldReturnAListOfMultiPrinters() throws Exception {

        when( multiPrinterService.findAllMultiPrinters() ).thenReturn( List.of( multiPrinterDTO ) );
        ResponseEntity< List< MultiPrinterDTO > > multiPrinterDTOList = multiPrinterController.findAllMultiPrinters();
        assertNotNull( multiPrinterDTOList );
        assertEquals( 1, Objects.requireNonNull( multiPrinterDTOList.getBody() ).size() );
        assertEquals( multiPrinterDTO, multiPrinterDTOList.getBody().get( 0 ) );

        mockMvc.perform( get( "/api/v1/multi-printer" ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect( jsonPath( "$[0].brand").value( "Epson" ) )
                .andExpect( jsonPath( "$[0].serialNumber").value( "x1x2x3" ) );
    }

    @Test
    @DisplayName( "Should return a MultiPrinter by id" )
    void shouldReturnAMultiPrinterById() throws Exception {

        when( multiPrinterService.findMultiPrinterById( 1 ) ).thenReturn( multiPrinterDTO );
        ResponseEntity< MultiPrinterDTO > resultDTO = multiPrinterController.findMultiPrinterById( 1 );
        assertNotNull( resultDTO );
        assertEquals( multiPrinterDTO, resultDTO.getBody() );

        mockMvc.perform( get( "/api/v1/multi-printer/1" ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$.id" ).value( 1 ) )
                .andExpect( jsonPath( "$.brand" ).value( "Epson" ) )
                .andExpect( jsonPath( "$.serialNumber" ).value( "x1x2x3" ) );
    }

    @Test
    @DisplayName( "Should return a list of MultiPrinter by customer id" )
    void shouldReturnAListOfMultiPrintersByCustomerId() throws Exception {

        when( multiPrinterService.findAllMultiPrintersByCustomerId( 1L ) )
                                                                            .thenReturn( List.of( multiPrinterDTO ) );
        ResponseEntity< List< MultiPrinterDTO > > resultDTO = multiPrinterController
                                                                              .findAllMultiPrintersByCustomerId( 1L );
        assertNotNull( resultDTO );
        assertEquals( 1, Objects.requireNonNull( resultDTO.getBody() ).size() );
        assertEquals( multiPrinterDTO, resultDTO.getBody().get( 0 ) );

        mockMvc.perform( get( "/api/v1/multi-printer/customer/{customerId}", 1 )
                .contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$[0].id" ).value( 1 ) )
                .andExpect( jsonPath( "$[0].brand" ).value( "Epson" ) )
                .andExpect( jsonPath( "$[0].serialNumber" ).value( "x1x2x3" ) );
    }

    @Test
    @DisplayName( "Should save a MultiPrinter" )
    void shouldSaveMultiPrinter() throws Exception {

        when(multiPrinterService.saveMultiPrinter(multiPrinterDTO)).thenReturn(multiPrinterDTO);

        mockMvc.perform(post("/api/v1/multi-printer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(multiPrinterDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/multi-printer/save/1"));
    }

    private static String asJsonString( MultiPrinterDTO obj ) throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule( new JavaTimeModule() );
        return mapper.writeValueAsString( obj );
    }

    @Test
    @DisplayName( "Should set up a client on a MultiPrinter" )
    void shouldSetUpClientOnAMultiPrinter() throws Exception {
        when( multiPrinterService.setUpClientOnAMultiPrinter( 1, 1L ) ).thenReturn( multiPrinterDTO );

        ResponseEntity< MultiPrinterDTO > multiPrinterDTOResponse = multiPrinterController.setUpClientOnAMultiPrinter( 1, 1L );
        assertNotNull( multiPrinterDTOResponse );
        assertEquals( multiPrinterDTO, multiPrinterDTOResponse.getBody() );
        mockMvc.perform( patch( "/api/v1/multi-printer/set-customer?id=1&customerId=1" ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
    }

    @Test
    @DisplayName( "Should un set up a customer from a MultiPrinter by id" )
    void shouldUnSetUpACustomerFromAMultiPrinterById() throws Exception {

        ResponseEntity<Void> response = multiPrinterController.unSetUpCustomerFromMultiPrinterById(1);
        verify(multiPrinterService).unSetUpCustomerFromMultiPrinterById(1);
        assertThat(response.getStatusCode()).isEqualTo( HttpStatus.NO_CONTENT);

        mockMvc.perform( patch( "/api/v1/multi-printer/unset-customer/1" ) )
                .andExpect( status().isNoContent() );
    }

    private void start() {

        multiPrinterDTO = oneMultiPrinter().nowDTO();
    }

}