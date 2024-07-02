package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Log4j2
@RestController
@RequestMapping( "/api/v1/customers/pf" )
public class NaturalPersonCustomerController {

    private final NaturalPersonCustomerService naturalPersonCustomerService;

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

}
