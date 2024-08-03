package br.com.copyimagem.msmonthlypayment.core.domain.representations;

import br.com.copyimagem.msmonthlypayment.core.domain.enums.PrinterType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultiPrinterDTO {

    private Integer id;

    private String brand;

    private String model;

    private String serialNumber;

    private Double machineValue;

    private String machineStatus;

    private PrinterType printType;

    private Integer impressionCounterInitial;

    private Integer impressionCounterBefore;

    private Integer impressionCounterNow;

    private Integer printingFranchise;

    private Double monthlyPrinterAmount;

    private Long customer_id;

}
