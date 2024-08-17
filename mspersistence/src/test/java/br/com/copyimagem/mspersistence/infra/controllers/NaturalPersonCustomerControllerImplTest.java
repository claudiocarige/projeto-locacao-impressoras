package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import br.com.copyimagem.mspersistence.infra.controllers.impl.NaturalPersonCustomerControllerImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class NaturalPersonCustomerControllerImplTest {

    public static final long ID1L = 1L;

    public static final String CPF = "156.258.240-29";

    private NaturalPersonCustomer customerPf;

    private NaturalPersonCustomerDTO customerPfDTO;

    @Mock
    private NaturalPersonCustomerService naturalPersonCustomerService;

    @InjectMocks
    private NaturalPersonCustomerControllerImpl naturalPersonCustomerControllerImpl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( naturalPersonCustomerControllerImpl ).build();
        start();
    }

    @Test
    @DisplayName("Should return a list of NaturalPersonCustomers")
    void shouldReturnAListOfNaturalPersonCustomers() throws Exception {
        when(naturalPersonCustomerService.findAllNaturalPersonCustomer()).thenReturn( List.of(customerPfDTO));
        ResponseEntity<List<NaturalPersonCustomerDTO>> allNaturalPersonCustomers = naturalPersonCustomerControllerImpl
                .getAllNaturalPersonCustomers();
        assertNotNull(allNaturalPersonCustomers);
        mockMvc.perform(get("/api/v1/customers/pf/all")
                        .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Should return a NaturalPersonCustomer by id")
    void shouldReturnANaturalPersonCustomerById() throws Exception {

        when(naturalPersonCustomerService.findNaturalPersonCustomerById(customerPf.getId()))
                .thenReturn(customerPfDTO);

        mockMvc.perform(get("/api/v1/customers/pf/{id}", customerPfDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerPfDTO.getId()))
                .andExpect(jsonPath("$.clientName").value(customerPfDTO.getClientName())); // Add assertions for other fields
        verify(naturalPersonCustomerService, times(1))
                .findNaturalPersonCustomerById(customerPf.getId());
    }

    @Test
    @DisplayName("Should save a NaturalPersonCustomer")
    void shouldSaveANaturalPersonCustomer() throws Exception {

        when(naturalPersonCustomerService.saveNaturalPersonCustomer(customerPfDTO)).thenReturn(customerPfDTO);
        mockMvc.perform(post("/api/v1/customers/pf/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(customerPfDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/customers/pf/save/1"));

        verify(naturalPersonCustomerService, times(1)).saveNaturalPersonCustomer(customerPfDTO);
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
        customerPf = NaturalPersonCustomerBuilder.oneNaturalPersonCustomer()
                                                                        .withId( ID1L ).withCpf( CPF ).nowCustomerPF();
        customerPfDTO = NaturalPersonCustomerBuilder.oneNaturalPersonCustomer()
                                                                     .withId( ID1L ).withCpf( CPF ).nowCustomerPFDTO();
    }
}