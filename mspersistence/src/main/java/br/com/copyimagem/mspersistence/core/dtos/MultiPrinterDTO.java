package br.com.copyimagem.mspersistence.core.dtos;

import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.domain.enums.PrinterType;
import lombok.*;

import java.util.Objects;


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

    private String customer_id;

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

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        MultiPrinterDTO that = ( MultiPrinterDTO ) o;
        return Objects.equals( id, that.id ) && Objects.equals( serialNumber, that.serialNumber );
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, serialNumber );
    }

}
