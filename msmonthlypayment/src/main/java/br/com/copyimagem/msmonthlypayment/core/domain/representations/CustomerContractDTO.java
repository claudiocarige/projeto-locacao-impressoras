package br.com.copyimagem.msmonthlypayment.core.domain.representations;


public record CustomerContractDTO( Long id,
                                   Integer printingFranchisePB,
                                   Integer printingFranchiseColor,
                                   Double printerTypePBRate,
                                   Double printerTypeColorRate) {}

