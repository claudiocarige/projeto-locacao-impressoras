package br.com.copyimagem.mspersistence.core.usecases.interfaces;


import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;

import java.util.List;


public interface MultiPrinterService {

    MultiPrinterDTO findMultiPrinterById( Integer id );

    List< MultiPrinterDTO > findAllMultiPrinters();

    List< MultiPrinterDTO > findAllMultiPrintersByCustomerId( Long customer_Id );

    MultiPrinterDTO saveMultiPrinter( MultiPrinterDTO multiPrinterDTO );

    MultiPrinterDTO setUpClientOnAMultiPrinter( Integer id, Long customer_Id );

    void deleteMultiPrinter( Integer id );

    MultiPrinterDTO deleteCustomerFromMultiPrinter( Integer id );

}
