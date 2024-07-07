package br.com.copyimagem.mspersistence.core.dtos;

import br.com.copyimagem.mspersistence.core.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonthlyPaymentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @JsonFormat( pattern = "dd-MM-yyyy" )
    private LocalDate expirationDate;

    @JsonFormat( pattern = "dd-MM-yyyy" )
    private LocalDate paymentDate;

    private PaymentStatus paymentStatus;

    private Long customerId;

}
