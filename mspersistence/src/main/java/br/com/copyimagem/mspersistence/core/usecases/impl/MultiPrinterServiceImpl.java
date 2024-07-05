package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MultiPrinterRepository;

import java.util.List;


public class MultiPrinterServiceImpl implements MultiPrinterService {

    private final MultiPrinterRepository multiPrinterRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    public MultiPrinterServiceImpl( MultiPrinterRepository multiPrinterRepository, ConvertObjectToObjectDTOService convertObjectToObjectDTOService ) {

        this.multiPrinterRepository = multiPrinterRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
    }


    @Override
    public MultiPrinterDTO findMultiPrinterById( Integer id ) {

        return null;
    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrinters() {

        return List.of();
    }

}
