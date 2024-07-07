package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.mspersistence.core.domain.enums.PaymentStatus;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MonthlyPaymentService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MonthlyPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class MonthlyPaymentServiceImpl implements MonthlyPaymentService {

    private final MultiPrinterService multiPrinterService;

    private final CustomerService customerService;

    private final MonthlyPaymentRepository monthlyPaymentRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

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
        monthlyPayment.setExpirationDate( LocalDate.now().plusDays( 5 ) );
        monthlyPayment.setInvoiceNumber( monthlyPaymentRequest.invoiceNumber() );
        monthlyPayment.setTicketNumber( monthlyPaymentRequest.ticketNumber() );
        monthlyPayment.setCustomer( customerService.returnCustomer( monthlyPaymentRequest.customerId() ) );
        monthlyPayment.setPrintingFranchisePB( monthlyPayment.getCustomer()
                .getCustomerContract().getPrintingFranchisePB() );
        monthlyPayment.setPrintingFranchiseColor( monthlyPayment.getCustomer()
                .getCustomerContract().getPrintingFranchiseColor() );
        monthlyPayment.setPaymentDate( LocalDate.now().plusDays( 9 ) );
        monthlyPayment.setPaymentStatus( PaymentStatus.PENDENTE );
        getInformationFromMultiPrinter( monthlyPayment );
    }


    private void getInformationFromMultiPrinter( MonthlyPayment monthlyPayment ) {

        var excessAmountPrinterColor = 0.0;
        var excessAmountPrinterPB = 0.0;
        var valueOfPrinters = 0.0;
        var quantPB = 0;
        var quantColor = 0;
        List< MultiPrinterDTO > multiPrinterDTOList = multiPrinterService
                .findAllMultiPrintersByCustomerId( monthlyPayment.getCustomer().getId() );

        for( MultiPrinterDTO multiPrinterDTO : multiPrinterDTOList ) {
            var excessValue = ( multiPrinterDTO.sumQuantityPrints()
                    - multiPrinterDTO.getPrintingFranchise() ) * multiPrinterDTO.getPrintType().getRate();
            if( multiPrinterDTO.getPrintType().getType().contains( "color" ) ) {
                quantColor += (
                  monthlyPayment.getQuantityPrintsColor() == null ) ? 0 : monthlyPayment.getQuantityPrintsColor()
                                                                                 + multiPrinterDTO.sumQuantityPrints();
                excessAmountPrinterColor += excessValue;
            } else {
                quantColor += (
                  monthlyPayment.getQuantityPrintsPB() == null ) ? 0 : monthlyPayment.getQuantityPrintsPB()
                                                                                 + multiPrinterDTO.sumQuantityPrints();
                excessAmountPrinterPB += excessValue;
            }
            valueOfPrinters += multiPrinterDTO
                                    .getMonthlyPrinterAmount() == null ? 0 : multiPrinterDTO.getMonthlyPrinterAmount();
        }
        monthlyPayment.setExcessValuePrintsPB( excessAmountPrinterPB );
        monthlyPayment.setExcessValuePrintsColor( excessAmountPrinterColor );
        monthlyPayment.setMonthlyAmount( valueOfPrinters + excessAmountPrinterColor + excessAmountPrinterPB );
        monthlyPayment.setQuantityPrintsPB( quantPB );
        monthlyPayment.setQuantityPrintsColor( quantColor );
    }

}
