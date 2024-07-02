package br.com.copyimagem.mspersistence.infra.controllers;


import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
