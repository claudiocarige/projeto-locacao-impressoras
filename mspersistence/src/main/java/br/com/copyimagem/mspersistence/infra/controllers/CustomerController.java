package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Log4j2
@RestController
@RequestMapping( "/api/v1/customers" )
public class CustomerController {


    private final CustomerService customerService;

    @Autowired
    public CustomerController( CustomerService customerService ) {

        this.customerService = customerService;
    }

    @GetMapping( value = "/search-client" )
    public ResponseEntity< CustomerResponseDTO > searchCustomerByParams(
            @RequestParam( "typeParam" ) String typeParam,
            @RequestParam( "valueParam" ) String valueParam ) {

        log.info( String.format( "[ INFO ] Search for customers by : %s -- with value : %s.",
                typeParam.toUpperCase(), valueParam ) );
        CustomerResponseDTO response = customerService.searchCustomer( typeParam, valueParam );
        return ResponseEntity.ok().body( response );
    }

    @GetMapping( value = "/search-client-all" )
    public ResponseEntity< List< CustomerResponseDTO > > searchAllCustomers() {

        log.info( String.format( "[ INFO ] Search for all customers --- { %s }", CustomerController.class ) );
        return ResponseEntity.ok().body( customerService.searchAllCustomers() );
    }

}
