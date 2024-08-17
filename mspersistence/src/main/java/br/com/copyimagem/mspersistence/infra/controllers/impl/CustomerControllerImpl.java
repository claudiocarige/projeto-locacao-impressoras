package br.com.copyimagem.mspersistence.infra.controllers.impl;

import br.com.copyimagem.mspersistence.core.dtos.CustomerContractDTO;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.infra.controllers.CustomerController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@RestController
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerControllerImpl( CustomerService customerService ) {

        this.customerService = customerService;
    }

    @Override
    public ResponseEntity< CustomerResponseDTO > searchCustomerByParams(
                                                        @RequestParam( "typeParam" ) String typeParam,
                                                        @RequestParam( "valueParam" ) String valueParam ) {

        log.info( String.format( "[ INFO ] Search for customers by : %s -- with value : %s.",
                typeParam.toUpperCase(), valueParam ) );
        CustomerResponseDTO response = customerService.searchCustomer( typeParam, valueParam );
        return ResponseEntity.ok().body( response );
    }

    @Override
    public ResponseEntity< CustomerContractDTO > searchCustomerContract( @PathVariable Long customerId ) {

        log.info( String.format( "[ INFO ] Search for contract customer by customer id : %s.", customerId ) );
        return ResponseEntity.ok().body( customerService.findCustomerContractByCustomerId( customerId ) );
    }

    @Override
    public ResponseEntity< List< CustomerResponseDTO > > searchAllCustomers() {

        log.info( String.format( "[ INFO ] Search for all customers --- { %s }", CustomerControllerImpl.class ) );
        return ResponseEntity.ok().body( customerService.searchAllCustomers() );
    }

    @Override
    public ResponseEntity< List< CustomerResponseDTO > > searchFinancialSituation( @RequestParam( "situation" ) String situation ) {

        if( situation == null || ( ! situation.equals( "PAGO" ) && ! situation.equals( "PENDENTE" ) && ! situation.equals( "INADIMPLENTE" ) && ! situation.equals( "CANCELADO" ) ) ) {
            throw new IllegalArgumentException( "The argument is not correct" );
        }
        log.info( String.format( "[ INFO ] Search for all defaulting customers --- { %s }", CustomerControllerImpl.class ) );
        return ResponseEntity.ok().body( customerService.searchFinancialSituation( situation ) );
    }

    @Override
    public ResponseEntity< UpdateCustomerDTO > updateCustomerAttribute(
            @RequestParam( name = "attribute" ) String attribute,
            @RequestParam( name = "value" ) String value, @PathVariable Long id ) {

        UpdateCustomerDTO updateDto = customerService
                .updateCustomerAttribute( attribute, value, id );
        return ResponseEntity.ok().body( updateDto );
    }

}
