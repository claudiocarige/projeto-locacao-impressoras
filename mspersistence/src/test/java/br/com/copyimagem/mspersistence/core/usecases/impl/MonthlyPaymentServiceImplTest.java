package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.*;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MonthlyPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static br.com.copyimagem.mspersistence.core.domain.builders.MonthlyPaymentBuilder.oneMonthlyPayment;
import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class MonthlyPaymentServiceImplTest {


    private MonthlyPayment monthlyPayment;

    private MonthlyPaymentDTO monthlyPaymentDTO;

    private MonthlyPaymentRequest monthlyPaymentRequest;

    private MultiPrinterDTO multiPrinterDTO;

    private Customer customer;


    @Mock
    private MultiPrinterService multiPrinterService;

    @Mock
    private CustomerService customerService;

    @Mock
    private MonthlyPaymentRepository monthlyPaymentRepository;

    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Spy
    @InjectMocks
    private MonthlyPaymentServiceImpl monthlyPaymentServiceImpl;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        startEntities();
    }

    @Test
    @DisplayName( "Should create a monthly payment" )
    void shouldCreateAMonthlyPayment() {

        when( monthlyPaymentRepository.save( any( MonthlyPayment.class ) ) ).thenReturn( monthlyPayment );
        when( convertObjectToObjectDTOService.convertToMonthlyPaymentDTO( any( MonthlyPayment.class ) ) )
                                                                                      .thenReturn( monthlyPaymentDTO );
        when( customerService.returnCustomer( monthlyPaymentRequest.customerId() ) ).thenReturn( customer );
        when( multiPrinterService.findAllMultiPrintersByCustomerId( monthlyPaymentRequest.customerId() ) )
                                                                             .thenReturn( List.of( multiPrinterDTO ) );

        MonthlyPaymentDTO result = monthlyPaymentServiceImpl.createMonthlyPayment( monthlyPaymentRequest );

        assertNotNull( result );
        assertEquals( result.getCustomerId(), monthlyPaymentDTO.getCustomerId() );
        assertEquals( result.getInvoiceNumber(), monthlyPaymentRequest.invoiceNumber() );
        assertEquals( result.getTicketNumber(), monthlyPaymentRequest.ticketNumber() );
        assertEquals( result.getQuantityPrintsColor(), monthlyPaymentDTO.getQuantityPrintsColor() );
        assertEquals( result.getExcessValuePrintsPB(), monthlyPaymentDTO.getExcessValuePrintsPB() );
        assertEquals( result.getClass(), MonthlyPaymentDTO.class );
        verify( monthlyPaymentRepository ).save( any( MonthlyPayment.class ) );
    }

    @Test
    @DisplayName("Should return a MonthlyPayment By Id")
    void shouldReturnAMonthlyPaymentById() {
        when(monthlyPaymentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        MonthlyPaymentDTO result = monthlyPaymentServiceImpl.findMonthlyPaymentById(1L);
        assertNotNull(result);
        assertEquals(result.getCustomerId(), monthlyPaymentDTO.getCustomerId());
        assertEquals(result.getInvoiceNumber(), monthlyPaymentDTO.getInvoiceNumber());
        assertEquals(result.getTicketNumber(), monthlyPaymentDTO.getTicketNumber());
        assertEquals(result.getQuantityPrintsColor(), monthlyPaymentDTO.getQuantityPrintsColor());
        assertEquals(result.getExcessValuePrintsPB(), monthlyPaymentDTO.getExcessValuePrintsPB());
        assertEquals(result.getClass(), MonthlyPaymentDTO.class);
        verify(monthlyPaymentRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should return a List of MonthlyPayment By CustomerId")
    void shouldReturnAListOfMonthlyPaymentByCustomerId() {
        when(monthlyPaymentRepository.findAllMonthlyPaymentsByCustomerId(1L)).thenReturn(List.of(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        List<MonthlyPaymentDTO> result = monthlyPaymentServiceImpl.findAllMonthlyPaymentsByCustomerId(1L);
        assertNotNull(result);
        assertEquals( result.size(), 1);
        assertEquals(result.get(0).getInvoiceNumber(), monthlyPaymentDTO.getInvoiceNumber());
        assertEquals(result.get(0).getClass(), MonthlyPaymentDTO.class);
        verify(monthlyPaymentRepository).findAllMonthlyPaymentsByCustomerId(1L);
    }

    void startEntities() {

        monthlyPayment = oneMonthlyPayment().now();
        monthlyPaymentDTO = oneMonthlyPayment().nowDTO();
        monthlyPaymentRequest = new MonthlyPaymentRequest( 1L, "0001", "0002" );
        multiPrinterDTO = oneMultiPrinter().nowDTO();
        customer = new LegalPersonalCustomer();
        customer.setId( 1L );
        CustomerContract customerContract = new CustomerContract();
        customer.setCustomerContract( customerContract );
    }

}