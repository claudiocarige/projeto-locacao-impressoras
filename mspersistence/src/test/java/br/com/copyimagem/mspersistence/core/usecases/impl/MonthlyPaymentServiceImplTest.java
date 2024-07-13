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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.copyimagem.mspersistence.core.domain.builders.MonthlyPaymentBuilder.oneMonthlyPayment;
import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
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

    @InjectMocks
    @Spy
    private MonthlyPaymentServiceImpl monthlyPaymentServiceImpl;


    @BeforeEach
    void setUp() {
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
        assertEquals( monthlyPaymentDTO.getCustomerId(), result.getCustomerId());
        assertEquals( monthlyPaymentRequest.invoiceNumber(), result.getInvoiceNumber() );
        assertEquals( monthlyPaymentRequest.ticketNumber(), result.getTicketNumber() );
        assertEquals( 1000, result.getQuantityPrintsColor() );
        assertEquals( monthlyPaymentDTO.getExcessValuePrintsPB(), result.getExcessValuePrintsPB() );
        assertEquals( 3525.20,  result.getAmountPrinter() );
        assertEquals( MonthlyPaymentDTO.class, result.getClass() );
        verify( monthlyPaymentRepository ).save( any( MonthlyPayment.class ) );
        verify( convertObjectToObjectDTOService ).convertToMonthlyPaymentDTO( any( MonthlyPayment.class ) );
        verify( customerService ).returnCustomer( monthlyPaymentRequest.customerId() );
        verify( multiPrinterService ).findAllMultiPrintersByCustomerId( monthlyPaymentRequest.customerId() );
    }

    @Test
    @DisplayName("Should return a MonthlyPayment By Id")
    void shouldReturnAMonthlyPaymentById() {
        when(monthlyPaymentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        MonthlyPaymentDTO result = monthlyPaymentServiceImpl.findMonthlyPaymentById(1L);
        assertNotNull(result);
        assertEquals( monthlyPaymentDTO.getCustomerId(), result.getCustomerId() );
        assertEquals( monthlyPaymentDTO.getInvoiceNumber(), result.getInvoiceNumber() );
        assertEquals( monthlyPaymentDTO.getTicketNumber(), result.getTicketNumber() );
        assertEquals( monthlyPaymentDTO.getQuantityPrintsColor(), result.getQuantityPrintsColor() );
        assertEquals( monthlyPaymentDTO.getExcessValuePrintsPB(), result.getExcessValuePrintsPB() );
        assertEquals( MonthlyPaymentDTO.class, result.getClass() );
        verify(monthlyPaymentRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should return a List of MonthlyPayment By CustomerId")
    void shouldReturnAListOfMonthlyPaymentByCustomerId() {
        when(monthlyPaymentRepository.findAllMonthlyPaymentsByCustomerId(1L)).thenReturn(List.of(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        List<MonthlyPaymentDTO> result = monthlyPaymentServiceImpl.findAllMonthlyPaymentsByCustomerId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals( monthlyPaymentDTO.getInvoiceNumber(), result.get(0).getInvoiceNumber());
        assertEquals( MonthlyPaymentDTO.class, result.get(0).getClass());
        verify(monthlyPaymentRepository).findAllMonthlyPaymentsByCustomerId(1L);
    }

    @Test
    @DisplayName("Should return a List of MonthlyPayment By Attribute and Value")
    void shouldReturnAListOfMonthlyPaymentByAttributeAndValue() {
        when(monthlyPaymentRepository.findMonthlyPaymentByAttributeAndValue("monthPayment", 1)).thenReturn(List.of(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        List<MonthlyPaymentDTO> result = monthlyPaymentServiceImpl.findMonthlyPaymentByAttributeAndValue("monthPayment", "1");
        assertNotNull(result);
        assertEquals( 1, result.size());
        assertEquals(monthlyPaymentDTO.getInvoiceNumber(), result.get(0).getInvoiceNumber());
        assertEquals( MonthlyPaymentDTO.class, result.get(0).getClass());
        verify(monthlyPaymentRepository).findMonthlyPaymentByAttributeAndValue("monthPayment", 1);
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