package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Objects;

import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class MultiPrinterControllerTest {

    private MultiPrinter multiPrinter;

    private MultiPrinterDTO multiPrinterDTO;

    private MockMvc mockMvc;

    @Mock
    private MultiPrinterService multiPrinterService;

    @InjectMocks
    private MultiPrinterController multiprinterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( multiprinterController ).build();
        start();

    }

    @Test
    @DisplayName( "Should return a list of MultiPrinter" )
    void shouldReturnAListOfMultiPrinters() throws Exception {

        when( multiPrinterService.findAllMultiPrinters() ).thenReturn( List.of( multiPrinterDTO ) );
        ResponseEntity< List< MultiPrinterDTO > > multiPrinterDTOList = multiprinterController.findAllMultiPrinters();
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
        ResponseEntity< MultiPrinterDTO > resultDTO = multiprinterController.findMultiPrinterById( 1 );
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
        ResponseEntity< List< MultiPrinterDTO > > resultDTO = multiprinterController
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


    private void start() {

        multiPrinter = oneMultiPrinter().now();
        multiPrinterDTO = oneMultiPrinter().nowDTO();
    }

}