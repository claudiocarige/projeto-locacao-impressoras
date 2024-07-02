package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Log4j2
@RestController
@RequestMapping( "/api/v1/customers/pf" )
public class NaturalPersonCustomerController {

    private final NaturalPersonCustomerService naturalPersonCustomerService;

    @Autowired
    public NaturalPersonCustomerController( NaturalPersonCustomerService naturalPersonCustomerService ) {

        this.naturalPersonCustomerService = naturalPersonCustomerService;
    }

    @GetMapping( value = "/all" )
    public ResponseEntity< List< NaturalPersonCustomerDTO > > getAllNaturalPersonCustomers() {

        return ResponseEntity.ok().body( naturalPersonCustomerService.findAllNaturalPersonCustomer() );
    }

    @GetMapping( value = "/{id}" )
    public ResponseEntity< NaturalPersonCustomerDTO > getNaturalPersonCustomerById( @PathVariable Long id ) {

        log.info( "[ INFO ] HTTP call with GET method with path: ../customers/pf/{id}" );
        NaturalPersonCustomerDTO naturalPersonCustomerById =
                naturalPersonCustomerService.findNaturalPersonCustomerById( id );
        return ResponseEntity.ok().contentType( MediaType.APPLICATION_JSON ).body( naturalPersonCustomerById );
    }

    @PostMapping( value = "/save", produces = "application/json" )
    public ResponseEntity< HttpStatus > saveNaturalPersonCustomer(
            @Valid @RequestBody NaturalPersonCustomerDTO naturalPersonCustomerDTO ) {

        log.info( "[ INFO ] HTTP call with POST method with path: ../customers/pf/save" );
        NaturalPersonCustomerDTO naturalPersonCustomer1 =
                naturalPersonCustomerService.saveNaturalPersonCustomer( naturalPersonCustomerDTO );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path( "/{id}" )
                .buildAndExpand( naturalPersonCustomer1.getId() )
                .toUri();
        log.info( "[ INFO ] New NaturalPersonCustomer created with success." );
        return ResponseEntity.created( uri ).build();
    }

}
