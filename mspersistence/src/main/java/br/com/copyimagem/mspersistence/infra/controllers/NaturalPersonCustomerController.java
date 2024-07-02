package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@RequestMapping( "/api/v1/customers/pf" )
public class NaturalPersonCustomerController {

    private final NaturalPersonCustomerService naturalPersonCustomerService;

    public NaturalPersonCustomerController( NaturalPersonCustomerService naturalPersonCustomerService ) {

        this.naturalPersonCustomerService = naturalPersonCustomerService;
    }


}
