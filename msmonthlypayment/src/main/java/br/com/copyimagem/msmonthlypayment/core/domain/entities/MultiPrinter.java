package br.com.copyimagem.msmonthlypayment.core.domain.entities;


import br.com.copyimagem.msmonthlypayment.core.domain.enums.MachineStatus;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.PrinterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultiPrinter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String brand;

    private String model;

    private String serialNumber;

    private Double machineValue;

    private MachineStatus machineStatus;

    private PrinterType printType;

    private Integer impressionCounterInitial;

    private Integer impressionCounterBefore;

    private Integer impressionCounterNow;

    private Integer printingFranchise;

    private Double monthlyPrinterAmount;

    private Long customer_id;

}