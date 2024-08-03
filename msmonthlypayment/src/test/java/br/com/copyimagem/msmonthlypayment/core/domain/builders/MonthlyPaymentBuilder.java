package br.com.copyimagem.msmonthlypayment.core.domain.builders;

import br.com.copyimagem.msmonthlypayment.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.PaymentStatus;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;

import java.time.LocalDate;


public class MonthlyPaymentBuilder {


    private Long id;

    private Integer monthPayment;

    private Integer yearPayment;

    private Integer quantityPrintsPB;

    private Integer quantityPrintsColor;

    private Integer printingFranchisePB;

    private Integer printingFranchiseColor;

    private String invoiceNumber;

    private String ticketNumber;

    private Double amountPrinter;

    private Double monthlyAmount;

    private Double excessValuePrintsPB;

    private Double excessValuePrintsColor;

    private Double rateExcessColorPrinting;

    private Double rateExcessBlackAndWhitePrinting;

    private LocalDate expirationDate;

    private LocalDate paymentDate;

    private PaymentStatus paymentStatus;

    private Long customerId;

    private MonthlyPaymentBuilder() { }

    public static MonthlyPaymentBuilder oneMonthlyPayment() {

        MonthlyPaymentBuilder builder = new MonthlyPaymentBuilder();
        initializeDefaultData( builder );
        return builder;
    }

    private static void initializeDefaultData( MonthlyPaymentBuilder builder ) {

        builder.id = 1L;
        builder.monthPayment = 1;
        builder.yearPayment = 2024;
        builder.quantityPrintsPB = 1100;
        builder.quantityPrintsColor = 1000;
        builder.printingFranchisePB = 2000;
        builder.printingFranchiseColor = 2000;
        builder.invoiceNumber = "0001";
        builder.ticketNumber = "0002";
        builder.amountPrinter = 3525.20;
        builder.monthlyAmount = 200.0;
        builder.excessValuePrintsPB = 0.0;
        builder.excessValuePrintsColor = 0.0;
        builder.rateExcessColorPrinting = 0.15;
        builder.rateExcessBlackAndWhitePrinting = 0.04;
        builder.expirationDate = LocalDate.of( 2023, 11, 30 );
        builder.paymentDate = LocalDate.of( 2022, 12, 10 );
        builder.paymentStatus = PaymentStatus.PENDENTE;
        builder.customerId = 1L;

    }

    public MonthlyPaymentBuilder withId( Long id ) {

        this.id = id;
        return this;
    }


    public MonthlyPaymentBuilder withMonthPayment( Integer monthPayment ) {

        this.monthPayment = monthPayment;
        return this;
    }

    public MonthlyPaymentBuilder withYearPayment( Integer yearPayment ) {

        this.yearPayment = yearPayment;
        return this;
    }

    public MonthlyPaymentBuilder withQuantityPrintsPB( Integer quantityPrintsPB ) {

        this.quantityPrintsPB = quantityPrintsPB;
        return this;
    }

    public MonthlyPaymentBuilder withQuantityPrintsColor( Integer quantityPrintsColor ) {

        this.quantityPrintsColor = quantityPrintsColor;
        return this;
    }

    public MonthlyPaymentBuilder withPrintingFranchisePB( Integer printingFranchisePB ) {

        this.printingFranchisePB = printingFranchisePB;
        return this;
    }

    public MonthlyPaymentBuilder withPrintingFranchiseColor( Integer printingFranchiseColor ) {

        this.printingFranchiseColor = printingFranchiseColor;
        return this;
    }

    public MonthlyPaymentBuilder withInvoiceNumber( String invoiceNumber ) {

        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public MonthlyPaymentBuilder withTicketNumber( String ticketNumber ) {

        this.ticketNumber = ticketNumber;
        return this;
    }

    public MonthlyPaymentBuilder withAmountPrinter( Double amountPrinter ) {

        this.amountPrinter = amountPrinter;
        return this;
    }

    public MonthlyPaymentBuilder withMonthlyAmount( Double monthlyAmount ) {

        this.monthlyAmount = monthlyAmount;
        return this;
    }

    public MonthlyPaymentBuilder withExcessValuePrintsPB( Double excessValuePrintsPB ) {

        this.excessValuePrintsPB = excessValuePrintsPB;
        return this;
    }

    public MonthlyPaymentBuilder withExcessValuePrintsColor( Double excessValuePrintsColor ) {

        this.excessValuePrintsColor = excessValuePrintsColor;
        return this;
    }

    public MonthlyPaymentBuilder withRateExcessColorPrinting( Double rateExcessColorPrinting ) {

        this.rateExcessColorPrinting = rateExcessColorPrinting;
        return this;
    }

    public MonthlyPaymentBuilder withRateExcessBlackAndWhitePrinting( Double rateExcessBlackAndWhitePrinting ) {

        this.rateExcessBlackAndWhitePrinting = rateExcessBlackAndWhitePrinting;
        return this;
    }

    public MonthlyPaymentBuilder withExpirationDate( LocalDate expirationDate ) {

        this.expirationDate = expirationDate;
        return this;
    }

    public MonthlyPaymentBuilder withPaymentDate( LocalDate paymentDate ) {

        this.paymentDate = paymentDate;
        return this;
    }

    public MonthlyPaymentBuilder withPaymentStatus( PaymentStatus paymentStatus ) {

        this.paymentStatus = paymentStatus;
        return this;
    }

    public MonthlyPaymentBuilder withCustomer( Long customerId ) {

        this.customerId = customerId;
        return this;
    }

    public MonthlyPayment now() {

        return new MonthlyPayment(
                id, monthPayment, yearPayment, quantityPrintsPB,
                quantityPrintsColor, printingFranchisePB, printingFranchiseColor,
                invoiceNumber, ticketNumber, amountPrinter,
                monthlyAmount, excessValuePrintsPB, excessValuePrintsColor,
                rateExcessColorPrinting, rateExcessBlackAndWhitePrinting,
                expirationDate, paymentDate, paymentStatus, customerId
        );
    }


    public MonthlyPaymentDTO nowDTO() {

        return new MonthlyPaymentDTO(
                id, monthPayment, yearPayment, quantityPrintsPB,
                quantityPrintsColor, printingFranchisePB, printingFranchiseColor,
                invoiceNumber, ticketNumber, amountPrinter,
                monthlyAmount, excessValuePrintsPB, excessValuePrintsColor,
                rateExcessColorPrinting, rateExcessBlackAndWhitePrinting,
                expirationDate, paymentDate, paymentStatus.toString(), customerId
        );
    }

}