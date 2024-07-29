package br.com.copyimagem.msmonthlypayment.core.usecases.impl;


import br.com.copyimagem.msmonthlypayment.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.PaymentStatus;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.CustomerContractDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MultiPrinterDTO;
import br.com.copyimagem.msmonthlypayment.core.exceptions.NoSuchElementException;
import br.com.copyimagem.msmonthlypayment.core.usecases.MsMonthlyPaymentService;
import br.com.copyimagem.msmonthlypayment.infra.adapters.MsPersistenceServiceFeignClient;
import br.com.copyimagem.msmonthlypayment.infra.persistence.repositories.MsMonthlyPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class MsMonthlyPaymentServiceImpl implements MsMonthlyPaymentService {


    private final MsMonthlyPaymentRepository msMonthlyPaymentRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    private final MsPersistenceServiceFeignClient msPersistenceServiceFeignClient;

    public MsMonthlyPaymentServiceImpl( MsMonthlyPaymentRepository msMonthlyPaymentRepository,
                                        ConvertObjectToObjectDTOService convertObjectToObjectDTOService, MsPersistenceServiceFeignClient msPersistenceServiceFeignClient ) {

        this.msMonthlyPaymentRepository = msMonthlyPaymentRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
        this.msPersistenceServiceFeignClient = msPersistenceServiceFeignClient;
    }


    @Override
    public MonthlyPaymentDTO createMonthlyPayment( MonthlyPaymentRequest monthlyPaymentRequest ) {

        MonthlyPayment monthlyPayment = new MonthlyPayment();
        insertDataInMonthlyPayment( monthlyPaymentRequest, monthlyPayment );
        return convertObjectToObjectDTOService
                .convertToMonthlyPaymentDTO( msMonthlyPaymentRepository.save( monthlyPayment ) );
    }

    private void insertDataInMonthlyPayment(
        MonthlyPaymentRequest monthlyPaymentRequest, MonthlyPayment monthlyPayment ) {

        monthlyPayment.setMonthPayment( LocalDate.now().getMonthValue() );
        monthlyPayment.setYearPayment( LocalDate.now().getYear() );
        monthlyPayment.setPaymentDate( LocalDate.now().plusDays( 9 ) );
        monthlyPayment.setExpirationDate( monthlyPayment.getPaymentDate().plusDays( 5 ) );
        monthlyPayment.setInvoiceNumber( monthlyPaymentRequest.invoiceNumber() );
        monthlyPayment.setTicketNumber( monthlyPaymentRequest.ticketNumber() );
        monthlyPayment.setCustomerId( monthlyPaymentRequest.customerId() );
        CustomerContractDTO contractDTO = msPersistenceServiceFeignClient.searchCustomerContract( monthlyPaymentRequest.customerId() );
        monthlyPayment.setPrintingFranchisePB(contractDTO.printingFranchisePB() );
        monthlyPayment.setPrintingFranchiseColor( contractDTO.printingFranchiseColor() );
        monthlyPayment.setRateExcessBlackAndWhitePrinting(
                contractDTO.printerTypePBRate());
        monthlyPayment.setRateExcessColorPrinting(
                contractDTO.printerTypeColorRate());
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
        var monthlyAmount = 0.0;
        List< MultiPrinterDTO > multiPrinterDTOList = msPersistenceServiceFeignClient.findAllMultiPrintersByCustomerId(
                monthlyPayment.getCustomerId() );

        for( MultiPrinterDTO multiPrinterDTO : multiPrinterDTOList ) {
            var sumPrinter = multiPrinterDTO.sumQuantityPrints();
            var excessValue = ( sumPrinter < multiPrinterDTO.getPrintingFranchise() ? 0
                    : sumPrinter - multiPrinterDTO.getPrintingFranchise() );
            if( multiPrinterDTO.getPrintType().getType().startsWith( "Color" ) ) {
                quantityPrintsColor += sumPrinter;
                excessValuePrintsColor += excessValue * multiPrinterDTO.getPrintType().getRate();
                amountPrinterColor += multiPrinterDTO.getMonthlyPrinterAmount() + excessValuePrintsColor ;

            } else {
                quantityPrintsPB += sumPrinter;
                excessValuePrintsPB += excessValue * multiPrinterDTO.getPrintType().getRate();
                amountPrinterPB += multiPrinterDTO.getMonthlyPrinterAmount() + excessValuePrintsPB;

            }
            monthlyAmount += multiPrinterDTO.getMonthlyPrinterAmount();
        }

        monthlyPayment.setMonthlyAmount( monthlyAmount );
        monthlyPayment.setExcessValuePrintsPB( excessValuePrintsPB );
        monthlyPayment.setExcessValuePrintsColor( excessValuePrintsColor );
        monthlyPayment.setQuantityPrintsPB( quantityPrintsPB );
        monthlyPayment.setQuantityPrintsColor( quantityPrintsColor );
        monthlyPayment.setAmountPrinter( amountPrinterPB + amountPrinterColor );
    }

    @Override
    public MonthlyPaymentDTO findMonthlyPaymentById( Long id ) {

        MonthlyPayment monthlyPayment = msMonthlyPaymentRepository.
                findById( id ).orElseThrow( () -> new NoSuchElementException( "Monthly Payment not found" ) );
        return convertObjectToObjectDTOService.convertToMonthlyPaymentDTO( monthlyPayment );
    }

    @Override
    public List<MonthlyPaymentDTO> findAllMonthlyPaymentsByCustomerId(Long customerId) {
        return msMonthlyPaymentRepository.findAllMonthlyPaymentsByCustomerId(customerId).stream()
                .map( convertObjectToObjectDTOService::convertToMonthlyPaymentDTO ).toList();
    }

    @Override
    public List<MonthlyPaymentDTO> findMonthlyPaymentByAttributeAndValue (String attribute, String valueAttribute) {
        return ifAttributeAndValue(attribute, valueAttribute).stream()
                .map( convertObjectToObjectDTOService::convertToMonthlyPaymentDTO ).toList();
    }
    private List< MonthlyPayment> ifAttributeAndValue( String attribute, String valueAttribute ) {

        if( attribute.equals( "monthPayment" ) || attribute.equals( "yearPayment" ) ) {
            return msMonthlyPaymentRepository
                    .findMonthlyPaymentByAttributeAndValue( attribute, Integer.valueOf( valueAttribute ) );
        } else if( attribute.equals( "paymentStatus" ) ) {
            return msMonthlyPaymentRepository
                    .findMonthlyPaymentByAttributeAndValue( attribute, PaymentStatus.valueOf( valueAttribute ) );
        } else if( attribute.equals( "excessValuePrintsPB" ) ) {
            return msMonthlyPaymentRepository
                    .findMonthlyPaymentByAttributeAndValue( attribute, Double.valueOf( valueAttribute ) );
        } else {
            throw new IllegalArgumentException( "Invalid attribute: " + attribute );
        }
    }


}
