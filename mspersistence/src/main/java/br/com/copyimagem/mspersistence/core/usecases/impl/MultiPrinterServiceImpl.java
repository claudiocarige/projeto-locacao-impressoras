package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalStateException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.ConvertObjectToObjectDTOService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MultiPrinterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
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

        log.info( "[ INFO ] Finding MultiPrinter by id: " + id );
        return convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterRepository
                .findById( id ).orElseThrow(
                        () -> new NoSuchElementException( "MultiPrint not found" ) ), MultiPrinterDTO.class );
    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrinters() {

        log.info( "[ INFO ] Finding all MultiPrinters." );
        return convertObjectToObjectDTOService.convertEntityAndDTOList(
                multiPrinterRepository.findAll(), MultiPrinterDTO.class );

    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrintersByCustomerId( Long customer_Id ) {

        log.info( "[ INFO ] Finding all MultiPrinters by CustomerId: " + customer_Id );
        return convertObjectToObjectDTOService.convertEntityAndDTOList(
                multiPrinterRepository.findAllByCustomerId( customer_Id ), MultiPrinterDTO.class );
    }

    @Override
    public MultiPrinterDTO saveMultiPrinter( MultiPrinterDTO multiPrinterDTO ) {

        log.info( "[ INFO ] Creating MultiPrinter." );
        checkSerialNumber( multiPrinterDTO.getSerialNumber() );
        MultiPrinter multiPrinter = multiPrinterRepository.save( convertObjectToObjectDTOService
                .convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class ) );
        log.info( "[ INFO ] Saved MultiPrinter with id: " + multiPrinter.getId() );
        return convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinter, MultiPrinterDTO.class );
    }

    @Override
    public MultiPrinterDTO setUpClientOnAMultiPrinter( Integer id, Long customer_Id ) {

        log.info( "[ INFO ] Setting up Customer on MultiPrinter." );
        Customer customer = customerRepository.findById( customer_Id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        if( multiPrinterDTO.getCustomer_id() != null ) {
            log.error( "This printer already has a customer." );
            throw new IllegalArgumentException( "This printer is already Customer." );
        }
        if( multiPrinterDTO.getPrintType().getType().startsWith( "Color" ) ) {
            log.info( "Setting up Customer on Color MultiPrinter." );
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchiseColor() );
            multiPrinterDTO.setPrintType( customer.getCustomerContract().getPrinterTypeColor() );
        } else {
            log.info( "Setting up Customer on PB MultiPrinter." );
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchisePB() );
            multiPrinterDTO.setPrintType( customer.getCustomerContract().getPrinterTypePB() );
        }
        multiPrinterDTO.setCustomer_id( customer_Id.toString() );
        multiPrinterDTO.setMachineStatus( MachineStatus.LOCADA );
        MultiPrinter multiPrinter =
                convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class );
        multiPrinter.setCustomer( customer );
        log.info( "[ INFO ] Updated MultiPrinter with id" );
        multiPrinterRepository.save( multiPrinter );
        return multiPrinterDTO;
    }

    @Override
    public void deleteMultiPrinter( Integer id ) {

        log.info( "[ INFO ] Deleting MultiPrinter by id: " + id );
        MultiPrinter multiPrinter = multiPrinterRepository.findById( id )
                .orElseThrow( () -> new NoSuchElementException( "MultiPrint not found" ) );
        if( multiPrinter.getCustomer() != null ) {
            log.error( "This printer already has a customer." );
            throw new IllegalArgumentException( "This printer cannot be deleted as it has a client " +
                    "linked. You need to unlink the customer first." );
        }
        log.info( "[ INFO ] Deleted MultiPrinter with id: " + id );
        multiPrinterRepository.deleteById( id );
    }

    @Override
    public MultiPrinterDTO unSetUpCustomerFromMultiPrinterById( Integer id ) {

        log.info( "[ INFO ] Unsetting up Customer from MultiPrinter." );
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        multiPrinterDTO.setCustomer_id( null );
        multiPrinterDTO.setMachineStatus( MachineStatus.DISPONIVEL );
        multiPrinterRepository.save(
                convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class ) );
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setMachineStatus( Integer id, String status ) {

        log.info( "[ INFO ] Setting Machine Status." );
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        int row;
        switch( status ) {
            case "LOCADA" -> {
                if( multiPrinterDTO.getCustomer_id() != null ) {
                    log.error( "The printer already has a customer." );
                    throw new IllegalStateException( "The printer already has a customer. " +
                            "You need to deselect customer" );
                }
                log.info( "Setting Machine Status to LOCADA." );
                row = multiPrinterRepository.updateMachineStatusById( id, MachineStatus.valueOf( status ) );
            }
            case "INATIVA", "DISPONIVEL" -> {
                log.info( "Setting Machine Status to INATIVA or DISPONIVEL." );
                row = multiPrinterRepository.updateMachineStatusById( id, MachineStatus.valueOf( status ) );
                unSetUpCustomerFromMultiPrinterById( multiPrinterDTO.getId() );
            }
            case "MANUTENCAO" -> {
                log.info( "Setting Machine Status to MANUTENCAO." );
                row = multiPrinterRepository
                        .updateMachineStatusById( id, MachineStatus.valueOf( status ) );
            }
            default -> {
                log.error( "Invalid Status." );
                throw new IllegalArgumentException( "Invalid Status: " + status );
            }
        }
        if( row > 0 ) {
            multiPrinterDTO = findMultiPrinterById( id );
        } else {
            log.error( "No rows updated. Check the conditions and input values. [ {} ].", IllegalStateException.class );
            throw new IllegalStateException( "No rows updated. Check the conditions and input values." );
        }
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setImpressionCounter( Integer id, Integer counter, String attribute ) {

        log.info( "[ INFO ] Setting Impression Counter." );
        if( ! attribute.equals( "impressionCounterInitial" ) && ! attribute.equals( "impressionCounterBefore" ) &&
                ! attribute.equals( "impressionCounterNow" ) ) {
            throw new IllegalArgumentException( "Invalid attribute: " + attribute );
        }
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );

        int row;

        if( attribute.equals( "impressionCounterNow" ) ) {
            if( counter <= multiPrinterDTO.getImpressionCounterNow() ) {
                log.error( "The COUNTER value must be greater than the current value. [ {} ]", IllegalArgumentException.class );
                throw new IllegalArgumentException( "The COUNTER value must be greater than the current value." );
            }
            multiPrinterDTO.setImpressionCounterBefore( multiPrinterDTO.getImpressionCounterNow() );
            multiPrinterRepository.updateImpressionCounterByAttribute(
                    id, multiPrinterDTO.getImpressionCounterNow(), "impressionCounterBefore" );
            row = multiPrinterRepository.updateImpressionCounterByAttribute( id, counter, attribute );
        } else if( attribute.equals( "impressionCounterInitial" ) ) {
            log.info( "Setting Impression Counter Initial." );
            multiPrinterDTO.setImpressionCounterInitial( counter );
            multiPrinterDTO.setImpressionCounterBefore( 0 );
            multiPrinterDTO.setImpressionCounterNow( 0 );
            var multiPrinter = multiPrinterRepository.save( convertObjectToObjectDTOService
                    .convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class ) );
            return convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinter, MultiPrinterDTO.class );
        } else {
            row = multiPrinterRepository.updateImpressionCounterByAttribute( id, counter, attribute );
        }

        if( row > 0 ) {
            return findMultiPrinterById( id );
        } else {
            throw new IllegalStateException( "No rows updated. Check the conditions and input values." );
        }
    }

    private void checkSerialNumber( String serialNumber ) {
        log.info( "[ INFO ] Checking Serial Number." );
        if( multiPrinterRepository.existsBySerialNumber( serialNumber ) ) {
            throw new IllegalArgumentException( "Serial number already exists" );
        }
    }

}
