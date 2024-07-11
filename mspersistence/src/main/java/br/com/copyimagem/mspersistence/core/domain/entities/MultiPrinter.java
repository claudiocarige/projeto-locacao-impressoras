package br.com.copyimagem.mspersistence.core.domain.entities;


import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.domain.enums.PrinterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultiPrinter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", nullable = false )
    private Integer id;

    private String brand;

    private String model;

    @Column( unique = true )
    private String serialNumber;

    private Double machineValue;

    @Enumerated( EnumType.STRING )
    private MachineStatus machineStatus;

    private PrinterType printType;

    private Integer impressionCounterInitial;

    private Integer impressionCounterBefore;

    private Integer impressionCounterNow;

    private Integer printingFranchise;

    private Double monthlyPrinterAmount;

    @ManyToOne
    @JoinColumn( name = "customer_id" )
    private Customer customer;

}