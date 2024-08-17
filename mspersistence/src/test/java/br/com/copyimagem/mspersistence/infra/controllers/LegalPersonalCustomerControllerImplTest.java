package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import br.com.copyimagem.mspersistence.infra.controllers.impl.LegalPersonalCustomerControllerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LegalPersonalCustomerControllerImplTest {

    public static final long ID1L = 1L;

    public static final String CNPJ = "14.124.420/0001-94";

    private LegalPersonalCustomer customerPj;

    private LegalPersonalCustomerDTO customerPjDTO;

    @Mock
    private LegalPersonalCustomerService legalPersonalCustomerService;

    @InjectMocks
    private LegalPersonalCustomerControllerImpl LegalPersonalCustomerControllerImpl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( LegalPersonalCustomerControllerImpl ).build();
        start();
    }

    @Test
    @DisplayName("Should return a list of LegalPersonalCustomers")
    void shouldReturnAListOfLegalPersonalCustomers() throws Exception {
        when(legalPersonalCustomerService.findAllLegalPersonalCustomer()).thenReturn( List.of(customerPjDTO));
        ResponseEntity<List<LegalPersonalCustomerDTO>> allLegalPersonalCustomerDTOs = LegalPersonalCustomerControllerImpl
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
                () ->  LegalPersonalCustomerControllerImpl.getLegalPersonalCustomerById(ID1L));
        String legalPersonalCustomerById = assertThrows(NoSuchElementException.class,
                () -> LegalPersonalCustomerControllerImpl.getLegalPersonalCustomerById(ID1L)).getMessage();
        assertEquals("Customer not found", legalPersonalCustomerById);
        verify(legalPersonalCustomerService, times(2)).findLegalPersonalCustomerById(ID1L);
    }

    @Test
    @DisplayName("Should save a LegalPersonalCustomer")
    void shouldSaveALegalPersonalCustomer() throws Exception {
        when(legalPersonalCustomerService.saveLegalPersonalCustomer(any(LegalPersonalCustomerDTO.class)))
                .thenReturn(customerPjDTO);

        mockMvc.perform(post("/api/v1/customers/pj/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(customerPjDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/customers/pj/save/1"));

        verify(legalPersonalCustomerService, times(1)).saveLegalPersonalCustomer(any(LegalPersonalCustomerDTO.class));
    }

    private static String toJsonString(final Object obj) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    private void start() {

        customerPj = oneLegalPersonalCustomer().withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJ();
        customerPjDTO = oneLegalPersonalCustomer().withId( ID1L ).withCnpj( CNPJ ).withPrimaryEmail( "carige@mail.com" ).nowCustomerPJDTO();
    }

}