package br.com.copyimagem.mspersistence.core.domain.entities;


import br.com.copyimagem.mspersistence.core.domain.enums.PrinterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@Entity
public class CustomerContract implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Integer printingFranchisePB;

    private Integer printingFranchiseColor;

    private Double monthlyAmount;

    private Short contractTime;

    @JsonFormat( pattern = "dd-MM-yyyy" )
    private LocalDate startContract;

    private PrinterType printerTypePB;

    private PrinterType printerTypeColor;

    public CustomerContract() {

        basicContract();
    }

    private void basicContract() {

        this.printingFranchisePB = 2000;
        this.printingFranchiseColor = 1000;
        this.monthlyAmount = 300.0;
        this.contractTime = 6;
        this.startContract = LocalDate.now();
        this.printerTypePB = PrinterType.LASER_BLACK_AND_WHITE_MEDIUM;
        this.printerTypeColor = PrinterType.INKJET_COLOR_MEDIUM;
    }

}
