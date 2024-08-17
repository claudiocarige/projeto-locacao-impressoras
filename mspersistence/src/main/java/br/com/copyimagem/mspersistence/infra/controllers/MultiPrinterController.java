package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping( "/api/v1/multi-printer" )
public interface MultiPrinterController {

    @GetMapping
    ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrinters();

    @GetMapping( "/{id}" )
    ResponseEntity< MultiPrinterDTO > findMultiPrinterById( @PathVariable Integer id );

    @GetMapping( "/customer/{customerId}" )
    ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrintersByCustomerId( @PathVariable Long customerId );

    @PostMapping( value = "/save")
    ResponseEntity< HttpStatus > saveMultiPrinter( @RequestBody MultiPrinterDTO multiPrinterDTO );

    @PatchMapping( "/set-customer" )
    ResponseEntity< MultiPrinterDTO > setUpClientOnAMultiPrinter( @RequestParam Integer id,
                                                                         @RequestParam Long customerId );

    @PatchMapping( "/unset-customer/{id}" )
    ResponseEntity< Void > unSetUpCustomerFromMultiPrinterById( @PathVariable Integer id );

    @PatchMapping( "/status" )
    ResponseEntity< MultiPrinterDTO > setMachineStatus( @RequestParam Integer id,
                                                               @RequestParam String status );

    @PatchMapping( "/impression-counter" )
    ResponseEntity< MultiPrinterDTO > setImpressionCounter( @RequestParam Integer id,
                                                                   @RequestParam Integer counter,
                                                                   @RequestParam String attribute );

    @DeleteMapping( "/{id}" )
    ResponseEntity< Void > deleteMultiPrinter( @PathVariable Integer id );
}
