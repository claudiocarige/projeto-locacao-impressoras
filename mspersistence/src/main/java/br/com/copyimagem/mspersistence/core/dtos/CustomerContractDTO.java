package br.com.copyimagem.mspersistence.core.dtos;


public record CustomerContractDTO( Long id,
                                   Integer printingFranchisePB,
                                   Integer printingFranchiseColor,
                                   Double printerTypePBRate,
                                   Double printerTypeColorRate) {}
