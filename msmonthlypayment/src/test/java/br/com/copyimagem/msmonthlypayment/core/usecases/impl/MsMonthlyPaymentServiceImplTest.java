package br.com.copyimagem.msmonthlypayment.core.usecases.impl;

import br.com.copyimagem.msmonthlypayment.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.PrinterType;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.CustomerContractDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MultiPrinterDTO;
import br.com.copyimagem.msmonthlypayment.infra.adapters.MsPersistenceServiceFeignClient;
import br.com.copyimagem.msmonthlypayment.infra.persistence.repositories.MsMonthlyPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.copyimagem.msmonthlypayment.core.domain.builders.MonthlyPaymentBuilder.oneMonthlyPayment;
import static br.com.copyimagem.msmonthlypayment.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith( MockitoExtension.class)
class MsMonthlyPaymentServiceImplTest {

    private MonthlyPayment monthlyPayment;

    private MonthlyPaymentDTO monthlyPaymentDTO;

    private MonthlyPaymentRequest monthlyPaymentRequest;

    private MultiPrinterDTO multiPrinterDTO;

    private CustomerContractDTO customerContractDTO;

    @Mock
    private MsPersistenceServiceFeignClient msPersistenceServiceFeignClient;

    @Mock
    private MsMonthlyPaymentRepository monthlyPaymentRepository;

    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @InjectMocks
    private MsMonthlyPaymentServiceImpl msMonthlyPaymentService;


    @BeforeEach
    void setUp() {

        startEntities();
    }

    @Test
    @DisplayName( "Should create a monthly payment" )
    void shouldCreateAMonthlyPayment() {

        when( monthlyPaymentRepository.save( any(MonthlyPayment.class)) ).thenReturn( monthlyPayment );
        when( convertObjectToObjectDTOService.convertToMonthlyPaymentDTO( any( MonthlyPayment.class ) ) )
                .thenReturn( monthlyPaymentDTO );
        when( msPersistenceServiceFeignClient
                .searchCustomerContract( monthlyPaymentRequest.customerId() ) ).thenReturn( customerContractDTO);
        when( msPersistenceServiceFeignClient.findAllMultiPrintersByCustomerId( monthlyPaymentRequest.customerId() ) )
                .thenReturn( List.of( multiPrinterDTO ) );

        MonthlyPaymentDTO result = msMonthlyPaymentService.createMonthlyPayment( monthlyPaymentRequest );

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
        verify( msPersistenceServiceFeignClient ).searchCustomerContract( monthlyPaymentRequest.customerId() );
        verify( msPersistenceServiceFeignClient )
                .findAllMultiPrintersByCustomerId( monthlyPaymentRequest.customerId() );
    }

    @Test
    @DisplayName("Should return a MonthlyPayment By Id")
    void shouldReturnAMonthlyPaymentById() {
        when(monthlyPaymentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(monthlyPayment));
        when(convertObjectToObjectDTOService.convertToMonthlyPaymentDTO(any(MonthlyPayment.class)))
                .thenReturn(monthlyPaymentDTO);
        MonthlyPaymentDTO result = msMonthlyPaymentService.findMonthlyPaymentById(1L);
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
        when(convertObjectToObjectDTOService
                .convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        List<MonthlyPaymentDTO> result = msMonthlyPaymentService.findAllMonthlyPaymentsByCustomerId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals( monthlyPaymentDTO.getInvoiceNumber(), result.get(0).getInvoiceNumber());
        assertEquals( MonthlyPaymentDTO.class, result.get(0).getClass());
        verify(monthlyPaymentRepository).findAllMonthlyPaymentsByCustomerId(1L);
    }

    @Test
    @DisplayName("Should return a List of MonthlyPayment By Attribute and Value")
    void shouldReturnAListOfMonthlyPaymentByAttributeAndValue() {
        when(monthlyPaymentRepository
                .findMonthlyPaymentByAttributeAndValue("monthPayment", 1)).thenReturn(List.of(monthlyPayment));
        when(convertObjectToObjectDTOService
                .convertToMonthlyPaymentDTO(any(MonthlyPayment.class))).thenReturn(monthlyPaymentDTO);
        List<MonthlyPaymentDTO> result = msMonthlyPaymentService
                .findMonthlyPaymentByAttributeAndValue("monthPayment", "1");
        assertNotNull(result);
        assertEquals( 1, result.size());
        assertEquals(monthlyPaymentDTO.getInvoiceNumber(), result.get(0).getInvoiceNumber());
        assertEquals( MonthlyPaymentDTO.class, result.get(0).getClass());
        verify(monthlyPaymentRepository).findMonthlyPaymentByAttributeAndValue("monthPayment", 1);
    }

    void startEntities() {

        monthlyPayment = oneMonthlyPayment().withId( 1L ).now();
        System.out.println(monthlyPayment.toString());
        monthlyPaymentDTO = oneMonthlyPayment().nowDTO();
        monthlyPaymentRequest = new MonthlyPaymentRequest( 1L, "0001", "0002" );
        multiPrinterDTO = oneMultiPrinter().nowDTO();
        customerContractDTO = new CustomerContractDTO(1L, 2000, 2000, PrinterType.LASER_COLOR_EASY.getRate(),
                PrinterType.LASER_BLACK_AND_WHITE_EASY.getRate());
    }

}