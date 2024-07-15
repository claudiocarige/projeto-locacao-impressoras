package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalStateException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MultiPrinterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        if(multiPrinterDTO.getPrintType().getType().startsWith( "Color" ) ){
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchiseColor());
            multiPrinterDTO.setPrintType(customer.getCustomerContract().getPrinterTypeColor() );
        }else{
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchisePB() );
            multiPrinterDTO.setPrintType(customer.getCustomerContract().getPrinterTypePB() );
        }
        multiPrinterDTO.setCustomer_id( customer_Id.toString() );
        multiPrinterDTO.setMachineStatus( MachineStatus.LOCADA );
        MultiPrinter multiPrinter = convertObjectToObjectDTOService.convertToMultiPrinter( multiPrinterDTO );
        multiPrinter.setCustomer( customer );
        multiPrinterRepository.save( multiPrinter );
        return multiPrinterDTO;
    }

    @Override
    public void deleteMultiPrinter( Integer id ) {

        MultiPrinter multiPrinter = multiPrinterRepository.findById( id )
                                           .orElseThrow( () -> new NoSuchElementException( "MultiPrint not found" ) );
        if( multiPrinter.getCustomer() != null ) {
            throw new IllegalArgumentException( "This printer cannot be deleted." );
        }
        multiPrinterRepository.deleteById( id );
    }

    @Override
    public MultiPrinterDTO unSetUpCustomerFromMultiPrinterById( Integer id ) {

        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        multiPrinterDTO.setCustomer_id( null );
        multiPrinterDTO.setMachineStatus( MachineStatus.DISPONIVEL );
        multiPrinterRepository.save( convertObjectToObjectDTOService.convertToMultiPrinter( multiPrinterDTO ) );
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setMachineStatus( Integer id, String status ) {

        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        int row;
        switch( status ) {
            case "LOCADA" -> {
                if( multiPrinterDTO.getCustomer_id() != null ) {
                    throw new IllegalStateException ( "The printer already has a customer. " +
                                                        "You need to deselect customer" );
                }
                row = multiPrinterRepository.updateMachineStatusById( id, MachineStatus.valueOf( status ) );
            }
            case "INATIVA", "DISPONIVEL" -> {
                row = multiPrinterRepository.updateMachineStatusById( id, MachineStatus.valueOf( status ) );
                unSetUpCustomerFromMultiPrinterById( multiPrinterDTO.getId() );
            }
            case "MANUTENCAO" -> row = multiPrinterRepository
                                                      .updateMachineStatusById( id, MachineStatus.valueOf( status ) );
            default -> throw new IllegalArgumentException( "Invalid Status: " + status );
        }
        if( row > 0 ) {
            multiPrinterDTO = findMultiPrinterById(id);
        } else {
            throw new IllegalStateException( "No rows updated. Check the conditions and input values." );
        }
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setImpressionCounter( Integer id, Integer counter, String attribute) {

        if ( !attribute.equals("impressionCounterInitial") && !attribute.equals("impressionCounterBefore") &&
             !attribute.equals("impressionCounterNow") ) {
            throw new IllegalArgumentException( "Invalid attribute: " + attribute );
        }
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById(id);

        int row = 0;

        if( attribute.equals("impressionCounterNow") ){
            if ( counter <= multiPrinterDTO.getImpressionCounterNow()) {
                throw new IllegalArgumentException( "The COUNTER value must be greater than the current value." );
            }
            multiPrinterDTO.setImpressionCounterBefore( multiPrinterDTO.getImpressionCounterNow() );
            multiPrinterRepository.updateImpressionCounterByAttribute(
                                     id, multiPrinterDTO.getImpressionCounterNow(), "impressionCounterBefore");
            row = multiPrinterRepository.updateImpressionCounterByAttribute( id, counter , attribute);
        }else if(attribute.equals("impressionCounterInitial")) {
            multiPrinterDTO.setImpressionCounterInitial( counter );
            multiPrinterDTO.setImpressionCounterBefore( 0 );
            multiPrinterDTO.setImpressionCounterNow( 0 );
            var multiPrinter = multiPrinterRepository.save( convertObjectToObjectDTOService
                                                                           .convertToMultiPrinter( multiPrinterDTO ) );
            return convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter );
        }else{
            row = multiPrinterRepository.updateImpressionCounterByAttribute( id, counter , attribute);
        }

        if ( row > 0) {
            return findMultiPrinterById(id);
        } else {
            throw new IllegalStateException ( "No rows updated. Check the conditions and input values." );
        }
    }

    private void checkSerialNumber( String serialNumber ) {

        if( multiPrinterRepository.existsBySerialNumber( serialNumber ) ) {
            throw new IllegalArgumentException( "Serial number already exists" );
        }
    }
}
