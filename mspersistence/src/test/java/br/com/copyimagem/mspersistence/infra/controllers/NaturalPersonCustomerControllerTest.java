package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.domain.builders.NaturalPersonCustomerBuilder;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class NaturalPersonCustomerControllerTest {

    public static final long ID1L = 1L;

    public static final String CPF = "156.258.240-29";

    private NaturalPersonCustomer customerPf;

    private NaturalPersonCustomerDTO customerPfDTO;

    @Mock
    private NaturalPersonCustomerService naturalPersonCustomerService;

    @InjectMocks
    private NaturalPersonCustomerController naturalPersonCustomerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( naturalPersonCustomerController ).build();
        start();
    }

    @Test
    @DisplayName("Should return a list of NaturalPersonCustomers")
    void shouldReturnAListOfNaturalPersonCustomers() throws Exception {
        when(naturalPersonCustomerService.findAllNaturalPersonCustomer()).thenReturn( List.of(customerPfDTO));
        ResponseEntity<List<NaturalPersonCustomerDTO>> allNaturalPersonCustomers = naturalPersonCustomerController
                .getAllNaturalPersonCustomers();
        assertNotNull(allNaturalPersonCustomers);
        mockMvc.perform(get("/api/v1/customers/pf/all")
                        .contentType( MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    private void start() {
        customerPf = NaturalPersonCustomerBuilder.oneNaturalPersonCustomer()
                                                                        .withId( ID1L ).withCpf( CPF ).nowCustomerPF();
        customerPfDTO = NaturalPersonCustomerBuilder.oneNaturalPersonCustomer()
                                                                     .withId( ID1L ).withCpf( CPF ).nowCustomerPFDTO();
    }
}