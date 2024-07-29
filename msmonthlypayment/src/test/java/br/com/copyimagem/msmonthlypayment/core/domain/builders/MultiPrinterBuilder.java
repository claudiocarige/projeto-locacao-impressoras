package br.com.copyimagem.msmonthlypayment.core.domain.builders;


import br.com.copyimagem.msmonthlypayment.core.domain.entities.MultiPrinter;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.MachineStatus;
import br.com.copyimagem.msmonthlypayment.core.domain.enums.PrinterType;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MultiPrinterDTO;


public class MultiPrinterBuilder {
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

    private MultiPrinterBuilder(){}

    public static MultiPrinterBuilder oneMultiPrinter() {
        MultiPrinterBuilder builder = new MultiPrinterBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    private static void initializeDefaultData(MultiPrinterBuilder builder) {
        builder.id = 1;
        builder.brand = "Epson";
        builder.model = "L5290";
        builder.serialNumber = "x1x2x3";
        builder.machineValue = 1000.0;
        builder.machineStatus = MachineStatus.DISPONIVEL;
        builder.printType = PrinterType.LASER_COLOR_EASY;
        builder.impressionCounterInitial = 1000;
        builder.impressionCounterBefore = 1000;
        builder.impressionCounterNow = 10000;
        builder.printingFranchise = 2000;
        builder.monthlyPrinterAmount = 300.0;
        builder.customer_id = 1L;
    }


    public MultiPrinterBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public MultiPrinterBuilder withBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public MultiPrinterBuilder withModel(String model) {
        this.model = model;
        return this;
    }

    public MultiPrinterBuilder withSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public MultiPrinterBuilder withMachineValue(Double machineValue) {
        this.machineValue = machineValue;
        return this;
    }

    public MultiPrinterBuilder withMachineStatus(MachineStatus machineStatus) {
        this.machineStatus = machineStatus;
        return this;
    }

    public MultiPrinterBuilder withPrintType(PrinterType printType) {
        this.printType = printType;
        return this;
    }

    public MultiPrinterBuilder withImpressionCounterInitial(Integer impressionCounterInitial) {
        this.impressionCounterInitial = impressionCounterInitial;
        return this;
    }

    public MultiPrinterBuilder withImpressionCounterBefore(Integer impressionCounterBefore) {
        this.impressionCounterBefore = impressionCounterBefore;
        return this;
    }

    public MultiPrinterBuilder withImpressionCounterNow(Integer impressionCounterNow) {
        this.impressionCounterNow = impressionCounterNow;
        return this;
    }

    public MultiPrinterBuilder withPrintingFranchise(Integer printingFranchise) {
        this.printingFranchise = printingFranchise;
        return this;
    }

    public MultiPrinterBuilder withMonthlyPrinterAmount(Double monthlyPrinterAmount) {
        this.monthlyPrinterAmount = monthlyPrinterAmount;
        return this;
    }

    public MultiPrinterBuilder withCustomer(Long customer_id) {
        this.customer_id = customer_id;
        return this;
    }

    public MultiPrinter now() {
        return new MultiPrinter(id, brand, model, serialNumber, machineValue, machineStatus,
                                printType, impressionCounterInitial, impressionCounterBefore, impressionCounterNow,
                                printingFranchise, monthlyPrinterAmount, customer_id);
    }

    public MultiPrinterDTO nowDTO() {
        return new MultiPrinterDTO(id, brand, model, serialNumber, machineValue, machineStatus,
                                   printType, impressionCounterInitial, impressionCounterBefore, impressionCounterNow,
                                   printingFranchise, monthlyPrinterAmount, 1L);
    }
}