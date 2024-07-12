package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.mspersistence.core.domain.enums.PaymentStatus;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MonthlyPaymentService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MonthlyPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class MonthlyPaymentServiceImpl implements MonthlyPaymentService {

    private final MultiPrinterService multiPrinterService;

    private final CustomerService customerService;

    private final MonthlyPaymentRepository monthlyPaymentRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Autowired
    public MonthlyPaymentServiceImpl( MultiPrinterService multiPrinterService, CustomerService customerService,
                                      MonthlyPaymentRepository monthlyPaymentRepository,
                                      ConvertObjectToObjectDTOService convertObjectToObjectDTOService ) {

        this.multiPrinterService = multiPrinterService;

        this.customerService = customerService;
        this.monthlyPaymentRepository = monthlyPaymentRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
    }

    @Override
    public MonthlyPaymentDTO createMonthlyPayment( MonthlyPaymentRequest monthlyPaymentRequest ) {

        MonthlyPayment monthlyPayment = new MonthlyPayment();
        insertDataInMonthlyPayment( monthlyPaymentRequest, monthlyPayment );
        return convertObjectToObjectDTOService
                                       .convertToMonthlyPaymentDTO( monthlyPaymentRepository.save( monthlyPayment ) );
    }

    private void insertDataInMonthlyPayment(
            MonthlyPaymentRequest monthlyPaymentRequest, MonthlyPayment monthlyPayment ) {

        monthlyPayment.setMonthPayment( LocalDate.now().getMonthValue() );
        monthlyPayment.setYearPayment( LocalDate.now().getYear() );
        monthlyPayment.setPaymentDate( LocalDate.now().plusDays( 9 ) );
        monthlyPayment.setExpirationDate( monthlyPayment.getPaymentDate().plusDays( 5 ) );
        monthlyPayment.setInvoiceNumber( monthlyPaymentRequest.invoiceNumber() );
        monthlyPayment.setTicketNumber( monthlyPaymentRequest.ticketNumber() );
        monthlyPayment.setCustomer( customerService.returnCustomer( monthlyPaymentRequest.customerId() ) );
        monthlyPayment.setPrintingFranchisePB( monthlyPayment.getCustomer()
                                                                    .getCustomerContract().getPrintingFranchisePB() );
        monthlyPayment.setPrintingFranchiseColor( monthlyPayment.getCustomer()
                                                                 .getCustomerContract().getPrintingFranchiseColor() );
        monthlyPayment.setRateExcessBlackAndWhitePrinting(
                                     monthlyPayment.getCustomer().getCustomerContract().getPrinterTypePB().getRate());
        monthlyPayment.setRateExcessColorPrinting(
                                 monthlyPayment.getCustomer().getCustomerContract().getPrinterTypeColor().getRate() );
        monthlyPayment.setPaymentStatus( PaymentStatus.PENDENTE );
        getInformationFromMultiPrinter( monthlyPayment );
    }

    private void getInformationFromMultiPrinter( MonthlyPayment monthlyPayment ) {

        var excessValuePrintsPB = 0.0;
        var excessValuePrintsColor = 0.0;
        var amountPrinterPB = 0.0;
        var amountPrinterColor = 0.0;
        var quantityPrintsPB = 0;
        var quantityPrintsColor = 0;
        List< MultiPrinterDTO > multiPrinterDTOList = multiPrinterService
                                            .findAllMultiPrintersByCustomerId( monthlyPayment.getCustomer().getId() );

        for( MultiPrinterDTO multiPrinterDTO : multiPrinterDTOList ) {
            var sumPrinter = multiPrinterDTO.sumQuantityPrints();
            var excessValue = ( sumPrinter < multiPrinterDTO.getPrintingFranchise() ? 0
                                                             : sumPrinter - multiPrinterDTO.getPrintingFranchise() );
            if( multiPrinterDTO.getPrintType().getType().startsWith( "Color" ) ) {
                quantityPrintsColor += sumPrinter;
                excessValuePrintsColor += excessValue * monthlyPayment.getRateExcessColorPrinting();
                amountPrinterColor += multiPrinterDTO.getMonthlyPrinterAmount() + excessValuePrintsColor;

//                amountPrinterColor += (multiPrinterDTO.getMonthlyPrinterAmount() != null
//                        ? multiPrinterDTO.getMonthlyPrinterAmount()
//                        : 0.0) + excessValuePrintsColor;
            } else {
                quantityPrintsPB += sumPrinter;
                excessValuePrintsPB += excessValue * monthlyPayment.getRateExcessBlackAndWhitePrinting();
                amountPrinterPB += multiPrinterDTO.getMonthlyPrinterAmount() + excessValuePrintsPB;

//                amountPrinterPB += (multiPrinterDTO.getMonthlyPrinterAmount() != null
//                        ? multiPrinterDTO.getMonthlyPrinterAmount()
//                        : 0.0) + excessValuePrintsPB;
            }
            monthlyPayment.setMonthlyAmount( multiPrinterDTO.getMonthlyPrinterAmount() );
        }

        monthlyPayment.setExcessValuePrintsPB( excessValuePrintsPB );
        monthlyPayment.setExcessValuePrintsColor( excessValuePrintsColor );
        monthlyPayment.setQuantityPrintsPB( quantityPrintsPB );
        monthlyPayment.setQuantityPrintsColor( quantityPrintsColor );
        monthlyPayment.setAmountPrinter( amountPrinterPB + amountPrinterColor );
    }

    @Override
    public MonthlyPaymentDTO findMonthlyPaymentById( Long id ) {

        MonthlyPayment monthlyPayment = monthlyPaymentRepository.
                        findById( id ).orElseThrow( () -> new NoSuchElementException( "Monthly Payment not found" ) );
        return convertObjectToObjectDTOService.convertToMonthlyPaymentDTO( monthlyPayment );
    }

    @Override
    public List<MonthlyPaymentDTO> findAllMonthlyPaymentsByCustomerId(Long customerId) {
        return monthlyPaymentRepository.findAllMonthlyPaymentsByCustomerId(customerId).stream()
                .map( convertObjectToObjectDTOService::convertToMonthlyPaymentDTO )
                        .toList();
    }

    @Override
    public List<MonthlyPaymentDTO> findMonthlyPaymentByAttributeAndValue (String attribute, String valueAttribute) {
        return ifAttributeAndValue(attribute, valueAttribute).stream()
                                         .map( convertObjectToObjectDTOService::convertToMonthlyPaymentDTO ).toList();
    }
    private List< MonthlyPayment> ifAttributeAndValue( String attribute, String valueAttribute ) {

        if( attribute.equals( "monthPayment" ) || attribute.equals( "yearPayment" ) ) {
            return monthlyPaymentRepository
                               .findMonthlyPaymentByAttributeAndValue( attribute, Integer.valueOf( valueAttribute ) );
        } else if( attribute.equals( "paymentStatus" ) ) {
            return monthlyPaymentRepository
                         .findMonthlyPaymentByAttributeAndValue( attribute, PaymentStatus.valueOf( valueAttribute ) );
        } else if( attribute.equals( "excessValuePrintsPB" ) ) {
            return monthlyPaymentRepository
                                .findMonthlyPaymentByAttributeAndValue( attribute, Double.valueOf( valueAttribute ) );
        } else {
            throw new IllegalArgumentException( "Invalid attribute: " + attribute );
        }
    }
}
