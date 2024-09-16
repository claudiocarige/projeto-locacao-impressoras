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

        return convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterRepository
                .findById( id ).orElseThrow(
                        () -> new NoSuchElementException( "MultiPrint not found" ) ), MultiPrinterDTO.class );
    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrinters() {

        return convertObjectToObjectDTOService.convertEntityAndDTOList(
                multiPrinterRepository.findAll(), MultiPrinterDTO.class );

    }

    @Override
    public List< MultiPrinterDTO > findAllMultiPrintersByCustomerId( Long customer_Id ) {

        return convertObjectToObjectDTOService.convertEntityAndDTOList(
                multiPrinterRepository.findAllByCustomerId( customer_Id ), MultiPrinterDTO.class );
    }

    @Override
    public MultiPrinterDTO saveMultiPrinter( MultiPrinterDTO multiPrinterDTO ) {

        checkSerialNumber( multiPrinterDTO.getSerialNumber() );
        MultiPrinter multiPrinter = multiPrinterRepository.save( convertObjectToObjectDTOService
                .convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class ) );
        return convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinter, MultiPrinterDTO.class );
    }

    @Override
    public MultiPrinterDTO setUpClientOnAMultiPrinter( Integer id, Long customer_Id ) {

        Customer customer = customerRepository.findById( customer_Id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        if( multiPrinterDTO.getCustomer_id() != null ) {
            throw new IllegalArgumentException( "This printer is already Customer." );
        }
        if( multiPrinterDTO.getPrintType().getType().startsWith( "Color" ) ) {
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchiseColor() );
            multiPrinterDTO.setPrintType( customer.getCustomerContract().getPrinterTypeColor() );
        } else {
            multiPrinterDTO.setPrintingFranchise( customer.getCustomerContract().getPrintingFranchisePB() );
            multiPrinterDTO.setPrintType( customer.getCustomerContract().getPrinterTypePB() );
        }
        multiPrinterDTO.setCustomer_id( customer_Id.toString() );
        multiPrinterDTO.setMachineStatus( MachineStatus.LOCADA );
        MultiPrinter multiPrinter =
                convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class );
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
        multiPrinterRepository.save(
                convertObjectToObjectDTOService.convertToEntityOrDTO( multiPrinterDTO, MultiPrinter.class ) );
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setMachineStatus( Integer id, String status ) {

        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );
        int row;
        switch( status ) {
            case "LOCADA" -> {
                if( multiPrinterDTO.getCustomer_id() != null ) {
                    throw new IllegalStateException( "The printer already has a customer. " +
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
            multiPrinterDTO = findMultiPrinterById( id );
        } else {
            throw new IllegalStateException( "No rows updated. Check the conditions and input values." );
        }
        return multiPrinterDTO;
    }

    @Override
    public MultiPrinterDTO setImpressionCounter( Integer id, Integer counter, String attribute ) {

        if( ! attribute.equals( "impressionCounterInitial" ) && ! attribute.equals( "impressionCounterBefore" ) &&
                ! attribute.equals( "impressionCounterNow" ) ) {
            throw new IllegalArgumentException( "Invalid attribute: " + attribute );
        }
        MultiPrinterDTO multiPrinterDTO = findMultiPrinterById( id );

        int row;

        if( attribute.equals( "impressionCounterNow" ) ) {
            if( counter <= multiPrinterDTO.getImpressionCounterNow() ) {
                throw new IllegalArgumentException( "The COUNTER value must be greater than the current value." );
            }
            multiPrinterDTO.setImpressionCounterBefore( multiPrinterDTO.getImpressionCounterNow() );
            multiPrinterRepository.updateImpressionCounterByAttribute(
                                           id, multiPrinterDTO.getImpressionCounterNow(), "impressionCounterBefore" );
            row = multiPrinterRepository.updateImpressionCounterByAttribute( id, counter, attribute );
        } else if( attribute.equals( "impressionCounterInitial" ) ) {
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

        if( multiPrinterRepository.existsBySerialNumber( serialNumber ) ) {
            throw new IllegalArgumentException( "Serial number already exists" );
        }
    }

}
