package br.com.copyimagem.msmonthlypayment.core.domain.representations;

import br.com.copyimagem.msmonthlypayment.core.domain.enums.MachineStatus;
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

    private MachineStatus machineStatus;

    private PrinterType printType;

    private Integer impressionCounterInitial;

    private Integer impressionCounterBefore;

    private Integer impressionCounterNow;

    private Integer printingFranchise;

    private Double monthlyPrinterAmount;

    private Long customer_id;

    public int sumQuantityPrints() {

        if( this.impressionCounterNow != null ) {
            if( this.impressionCounterBefore != null ) {
                int sum = 0;
                if( this.impressionCounterNow > this.impressionCounterBefore ) {
                    sum = this.impressionCounterNow - this.impressionCounterBefore;
                    this.impressionCounterBefore = this.impressionCounterNow;
                }
                return sum;
            } else {
                return this.impressionCounterNow - this.impressionCounterInitial;
            }
        }
        return 0;
    }
}
