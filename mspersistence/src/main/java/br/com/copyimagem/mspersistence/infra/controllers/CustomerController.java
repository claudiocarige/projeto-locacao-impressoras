package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.CustomerContractDTO;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping( "/api/v1/customers" )
public interface CustomerController {

    @GetMapping( value = "/search-client" )
    ResponseEntity< CustomerResponseDTO > searchCustomerByParams(
            @RequestParam( "typeParam" ) String typeParam,
            @RequestParam( "valueParam" ) String valueParam );

    @GetMapping( value = "/search-contract/{customerId}" )
    ResponseEntity< CustomerContractDTO > searchCustomerContract( @PathVariable Long customerId );

    @GetMapping( value = "/search-client-all" )
    ResponseEntity< List< CustomerResponseDTO > > searchAllCustomers();

    @GetMapping( value = "/search-financial-situation" )
    ResponseEntity< List< CustomerResponseDTO > > searchFinancialSituation( @RequestParam( "situation" ) String situation );

    @PatchMapping( value = "/{id}" )
    ResponseEntity< UpdateCustomerDTO > updateCustomerAttribute(
            @RequestParam( name = "attribute" ) String attribute,
            @RequestParam( name = "value" ) String value, @PathVariable Long id );
}
