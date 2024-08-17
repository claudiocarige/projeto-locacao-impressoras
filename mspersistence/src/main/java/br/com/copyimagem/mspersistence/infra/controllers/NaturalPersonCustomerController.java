package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping( "/api/v1/customers/pf" )
public interface NaturalPersonCustomerController {

    @GetMapping( value = "/all" )
    ResponseEntity< List< NaturalPersonCustomerDTO > > getAllNaturalPersonCustomers();

    @GetMapping( value = "/{id}" )
    ResponseEntity< NaturalPersonCustomerDTO > getNaturalPersonCustomerById( @PathVariable Long id );

    @PostMapping( value = "/save", produces = "application/json" )
    ResponseEntity< HttpStatus > saveNaturalPersonCustomer(
                                              @Valid @RequestBody NaturalPersonCustomerDTO naturalPersonCustomerDTO );

}
