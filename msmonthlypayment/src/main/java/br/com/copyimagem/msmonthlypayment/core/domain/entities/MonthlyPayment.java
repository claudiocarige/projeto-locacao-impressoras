package br.com.copyimagem.msmonthlypayment.core.domain.entities;


import br.com.copyimagem.msmonthlypayment.core.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MonthlyPayment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column( nullable = false )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Integer monthPayment;

    private Integer yearPayment;

    @Column( name = "quantity_prints_pb" )
    private Integer quantityPrintsPB;

    @Column( name = "quantity_prints_color" )
    private Integer quantityPrintsColor;

    @Column( name = "printing_franchise_pb" )
    private Integer printingFranchisePB;

    @Column( name = "printing_franchise_color" )
    private Integer printingFranchiseColor;

    private String invoiceNumber;

    private String ticketNumber;

    private Double amountPrinter;

    private Double monthlyAmount;

    @Column( name = "excess_value_prints_pb" )
    private Double excessValuePrintsPB;

    @Column( name = "excess_value_prints_color" )
    private Double excessValuePrintsColor;

    @Column( name = "rate_excess_color_printing" )
    private Double rateExcessColorPrinting;

    @Column( name = "rate_excess_black_and_white_printing" )
    private Double rateExcessBlackAndWhitePrinting;

    @JsonFormat( pattern = "dd-MM-yyyy" )
    private LocalDate expirationDate;

    @JsonFormat( pattern = "dd-MM-yyyy" )
    private LocalDate paymentDate;

    @Enumerated( EnumType.STRING )
    private PaymentStatus paymentStatus;

    private Long customerId;

}
