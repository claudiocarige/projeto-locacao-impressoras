package br.com.copyimagem.mspersistence.infra.controllers;


import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Log4j2
@RestController
@RequestMapping( "/api/v1/customers/pj" )
public class LegalPersonalCustomerController {

    private final LegalPersonalCustomerService legalPersonalCustomerService;

    public LegalPersonalCustomerController( LegalPersonalCustomerService legalPersonalCustomerService ) {

        this.legalPersonalCustomerService = legalPersonalCustomerService;
    }

    @GetMapping( value = "/all" )
    public ResponseEntity< List< LegalPersonalCustomerDTO > > getAllLegalPersonalCustomers() {

        return ResponseEntity.ok().body( legalPersonalCustomerService.findAllLegalPersonalCustomer() );
    }

    @GetMapping( value = "/{id}" )
    public ResponseEntity< LegalPersonalCustomerDTO > getLegalPersonalCustomerById( @PathVariable Long id ) {

        log.info( "[ INFO ] HTTP call with GET method with path: ../customers/pj/{id}" );
        LegalPersonalCustomerDTO legalPersonalCustomerById = legalPersonalCustomerService
                .findLegalPersonalCustomerById( id );
        return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON ).body( legalPersonalCustomerById );
    }

    @PostMapping( value = "/save", produces = "application/json" )
    public ResponseEntity< HttpStatus > saveLegalPersonalCustomer( @Valid
                                                   @RequestBody LegalPersonalCustomerDTO legalPersonalCustomerDTO ) {

        log.info( "[ INFO ] HTTP call with POST method with path: ../customers/pj/save" );
        LegalPersonalCustomerDTO legalPersonalCustomer = legalPersonalCustomerService
                .saveLegalPersonalCustomer( legalPersonalCustomerDTO );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path( "/{id}" )
                .buildAndExpand( legalPersonalCustomer.getId() )
                .toUri();
        log.info( "[ INFO ] New LegalPersonalCustomer created with success." );
        return ResponseEntity.created( uri ).build();
    }



}
