package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping( "/api/v1/multi-printer" )
public class MultiPrinterController {


    private final MultiPrinterService multiPrinterService;

    public MultiPrinterController( MultiPrinterService multiPrinterService ) {

        this.multiPrinterService = multiPrinterService;
    }

    @GetMapping
    public ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrinters() {

        return ResponseEntity.ok( multiPrinterService.findAllMultiPrinters() );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity< MultiPrinterDTO > findMultiPrinterById( @PathVariable Integer id ) {

        return ResponseEntity.ok( multiPrinterService.findMultiPrinterById( id ) );
    }

    @GetMapping( "/customer/{customerId}" )
    public ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrintersByCustomerId( @PathVariable Long customerId ) {

        return ResponseEntity.ok( multiPrinterService.findAllMultiPrintersByCustomerId( customerId ) );
    }

    @PostMapping( value = "/save")
    public ResponseEntity< HttpStatus > saveMultiPrinter( @RequestBody MultiPrinterDTO multiPrinterDTO ) {

        multiPrinterDTO = multiPrinterService.saveMultiPrinter( multiPrinterDTO );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path( "/{id}" )
                .buildAndExpand( multiPrinterDTO.getId() )
                .toUri();

        return ResponseEntity.created( uri ).build();
    }

    @PatchMapping( "/set-customer" )
    public ResponseEntity< MultiPrinterDTO > setUpClientOnAMultiPrinter( @RequestParam Integer id,
                                                                         @RequestParam Long customerId ) {

        return ResponseEntity.ok( multiPrinterService.setUpClientOnAMultiPrinter( id, customerId ) );
    }

    @PatchMapping( "/unset-customer/{id}" )
    public ResponseEntity< Void > unSetUpCustomerFromMultiPrinterById( @PathVariable Integer id ) {
        multiPrinterService.unSetUpCustomerFromMultiPrinterById( id );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping( "/status" )
    public ResponseEntity< MultiPrinterDTO > setMachineStatus( @RequestParam Integer id,
                                                               @RequestParam String status ) {

        return ResponseEntity.ok( multiPrinterService.setMachineStatus( id, status ) );
    }

    @PatchMapping( "/impression-counter" )
    public ResponseEntity< MultiPrinterDTO > setImpressionCounter( @RequestParam Integer id,
                                                                   @RequestParam Integer counter,
                                                                   @RequestParam String attribute ) {

        return ResponseEntity.ok( multiPrinterService.setImpressionCounter( id, counter, attribute ) );
    }
}
