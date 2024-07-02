package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
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

import static br.com.copyimagem.mspersistence.core.domain.builders.LegalPersonalCustomerBuilder.oneLegalPersonalCustomer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LegalPersonalCustomerControllerTest {

    public static final long ID1L = 1L;

    public static final String CNPJ = "14.124.420/0001-94";

    private LegalPersonalCustomer customerPj;

    private LegalPersonalCustomerDTO customerPjDTO;

    @Mock
    private LegalPersonalCustomerService legalPersonalCustomerService;

    @InjectMocks
    private LegalPersonalCustomerController LegalPersonalCustomerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( LegalPersonalCustomerController ).build();
        start();
    }

    @Test
    @DisplayName("Should return a list of LegalPersonalCustomers")
    void shouldReturnAListOfLegalPersonalCustomers() throws Exception {
        when(legalPersonalCustomerService.findAllLegalPersonalCustomer()).thenReturn( List.of(customerPjDTO));
        ResponseEntity<List<LegalPersonalCustomerDTO>> allLegalPersonalCustomerDTOs = LegalPersonalCustomerController
                .getAllLegalPersonalCustomers();
        assertNotNull(allLegalPersonalCustomerDTOs);
        mockMvc.perform(get("/api/v1/customers/pj/all")
                        .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Should return a LegalPersonalCustomer by id")
    void shouldReturnALegalPersonalCustomerById() throws Exception {
        when(legalPersonalCustomerService.findLegalPersonalCustomerById(customerPj.getId()))
                .thenReturn(customerPjDTO);

        mockMvc.perform(get("/api/v1/customers/pj/{id}", customerPjDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerPjDTO.getId()))
                .andExpect(jsonPath("$.clientName").value(customerPjDTO.getClientName()));
        verify(legalPersonalCustomerService, times(1))
                .findLegalPersonalCustomerById(customerPjDTO.getId());
    }

    @Test
    @DisplayName("Should return a exception when LegalPersonalCustomer not found")
    void shouldReturnAExceptionWhenLegalPersonalCustomerNotFound() {
        when(legalPersonalCustomerService.findLegalPersonalCustomerById(anyLong()))
                .thenThrow(new NoSuchElementException("Customer not found"));

        assertThrows(NoSuchElementException.class,
                () ->  LegalPersonalCustomerController.getLegalPersonalCustomerById(ID1L));
        String legalPersonalCustomerById = assertThrows(NoSuchElementException.class,
                () -> LegalPersonalCustomerController.getLegalPersonalCustomerById(ID1L)).getMessage();
        assertEquals("Customer not found", legalPersonalCustomerById);
        verify(legalPersonalCustomerService, times(2)).findLegalPersonalCustomerById(ID1L);
    }

    private void start() {

        customerPj = oneLegalPersonalCustomer().withId( ID1L ).withCnpj( CNPJ ).nowCustomerPJ();
        customerPjDTO = oneLegalPersonalCustomer().withId( ID1L ).withCnpj( CNPJ ).nowCustomerPJDTO();
    }

}