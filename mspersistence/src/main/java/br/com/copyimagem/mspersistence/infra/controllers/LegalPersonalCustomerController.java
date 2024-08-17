package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping( "/api/v1/customers/pj" )
public interface LegalPersonalCustomerController {

    @GetMapping( value = "/all" )
    ResponseEntity< List< LegalPersonalCustomerDTO > > getAllLegalPersonalCustomers();

    @GetMapping( value = "/{id}" )
    ResponseEntity< LegalPersonalCustomerDTO > getLegalPersonalCustomerById( @PathVariable Long id );

    @PostMapping( value = "/save", produces = "application/json" )
    ResponseEntity< HttpStatus > saveLegalPersonalCustomer( @Valid
                                                  @RequestBody LegalPersonalCustomerDTO legalPersonalCustomerDTO );

}
