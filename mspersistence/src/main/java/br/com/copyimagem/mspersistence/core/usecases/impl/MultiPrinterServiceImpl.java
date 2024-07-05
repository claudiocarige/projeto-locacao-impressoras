package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MultiPrinterRepository;

import java.util.List;


public class MultiPrinterServiceImpl implements MultiPrinterService {

    private final MultiPrinterRepository multiPrinterRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    private final CustomerRepository customerRepository;

    public MultiPrinterServiceImpl( MultiPrinterRepository multiPrinterRepository,
                                    ConvertObjectToObjectDTOService convertObjectToObjectDTOService,
                                    CustomerRepository customerRepository ) {

        this.multiPrinterRepository = multiPrinterRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
        this.customerRepository = customerRepository;
    }


    @Override
    public MultiPrinterDTO findMultiPrinterById( Integer id ) {

        return multiPrinterRepository.findById( id )
                .map( convertObjectToObjectDTOService::convertToMultiPrinterDTO )
                .orElseThrow(() -> new NoSuchElementException("MultiPrint not found"));
    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrinters() {

        return multiPrinterRepository.findAll().stream()
                .map( convertObjectToObjectDTOService::convertToMultiPrinterDTO )
                .toList();
    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrintersByCustomerId( Long customer_Id ) {

        return multiPrinterRepository.findAllByCustomerId( customer_Id )
                .stream()
                .map( convertObjectToObjectDTOService::convertToMultiPrinterDTO )
                .toList();
    }

    @Override
    public MultiPrinterDTO saveMultiPrinter( MultiPrinterDTO multiPrinterDTO ) {

        checkSerialNumber( multiPrinterDTO.getSerialNumber() );
        MultiPrinter multiPrinter = multiPrinterRepository.save( convertObjectToObjectDTOService
                .convertToMultiPrinter( multiPrinterDTO ) );
        return convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter );
    }

    @Override
    public MultiPrinterDTO setUpClientOnAMultiPrinter( Integer id, Long customer_Id ) {

        Customer customer = customerRepository.findById( customer_Id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        if( multiPrinterDTO.getCustomer_id() != null ) {
            throw new IllegalArgumentException( "This printer is already Customer." );
        }
        multiPrinterDTO.setCustomer_id( customer.getId().toString() );
        multiPrinterDTO.setMachineStatus( MachineStatus.LOCADA );
        MultiPrinter multiPrinter = convertObjectToObjectDTOService.convertToMultiPrinter( multiPrinterDTO );
        multiPrinter.setCustomer( customer );
        multiPrinterRepository.save( multiPrinter );
        return multiPrinterDTO;
    }

    private void checkSerialNumber( String serialNumber ) {

        if( multiPrinterRepository.existsBySerialNumber( serialNumber ) ) {
            throw new IllegalArgumentException( "Serial number already exists" );
        }
    }
}
