package br.com.copyimagem.mspersistence.infra.controllers.impl;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import br.com.copyimagem.mspersistence.infra.controllers.MultiPrinterController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
public class MultiPrinterControllerImpl implements MultiPrinterController {


    private final MultiPrinterService multiPrinterService;

    public MultiPrinterControllerImpl( MultiPrinterService multiPrinterService ) {

        this.multiPrinterService = multiPrinterService;
    }

    @Override
    public ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrinters() {

        return ResponseEntity.ok( multiPrinterService.findAllMultiPrinters() );
    }

    @Override
    public ResponseEntity< MultiPrinterDTO > findMultiPrinterById( @PathVariable Integer id ) {

        return ResponseEntity.ok( multiPrinterService.findMultiPrinterById( id ) );
    }

    @Override
    public ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrintersByCustomerId( @PathVariable Long customerId ) {

        return ResponseEntity.ok( multiPrinterService.findAllMultiPrintersByCustomerId( customerId ) );
    }

    @Override
    public ResponseEntity< HttpStatus > saveMultiPrinter( @RequestBody MultiPrinterDTO multiPrinterDTO ) {

        multiPrinterDTO = multiPrinterService.saveMultiPrinter( multiPrinterDTO );
        URI uri = ServletUriComponentsBuilder
                                             .fromCurrentRequest().path( "/{id}" )
                                             .buildAndExpand( multiPrinterDTO.getId() )
                                             .toUri();

        return ResponseEntity.created( uri ).build();
    }

    @Override
    public ResponseEntity< MultiPrinterDTO > setUpClientOnAMultiPrinter( @RequestParam Integer id,
                                                                         @RequestParam Long customerId ) {

        return ResponseEntity.ok( multiPrinterService.setUpClientOnAMultiPrinter( id, customerId ) );
    }

    @Override
    public ResponseEntity< Void > unSetUpCustomerFromMultiPrinterById( @PathVariable Integer id ) {

        multiPrinterService.unSetUpCustomerFromMultiPrinterById( id );
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity< MultiPrinterDTO > setMachineStatus( @RequestParam Integer id,
                                                               @RequestParam String status ) {

        return ResponseEntity.ok( multiPrinterService.setMachineStatus( id, status ) );
    }

    @Override
    public ResponseEntity< MultiPrinterDTO > setImpressionCounter( @RequestParam Integer id,
                                                                   @RequestParam Integer counter,
                                                                   @RequestParam String attribute ) {

        return ResponseEntity.ok( multiPrinterService.setImpressionCounter( id, counter, attribute ) );
    }

    @Override
    public ResponseEntity< Void > deleteMultiPrinter( @PathVariable Integer id ) {

        multiPrinterService.deleteMultiPrinter( id );
        return ResponseEntity.noContent().build();
    }

}
