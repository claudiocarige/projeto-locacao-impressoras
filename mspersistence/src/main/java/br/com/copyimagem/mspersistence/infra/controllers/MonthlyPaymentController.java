package br.com.copyimagem.mspersistence.infra.controllers;


import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MonthlyPaymentService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MonthlyPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping( "/api/v1/monthlypayment" )
public class MonthlyPaymentController {

    private final MonthlyPaymentRepository monthlyPaymentRepository;

    private final MonthlyPaymentService monthlyPaymentService;

    @Autowired
    public MonthlyPaymentController( MonthlyPaymentRepository monthlyPaymentRepository, MonthlyPaymentService monthlyPaymentService ) {

        this.monthlyPaymentRepository = monthlyPaymentRepository;
        this.monthlyPaymentService = monthlyPaymentService;
    }

    @GetMapping
    public List< MonthlyPaymentDTO > getMonthlyPayment() {

        return monthlyPaymentService.findAllMonthlyPaymentsByCustomerId( 1L );
    }

    @GetMapping( "/search")
    public List< MonthlyPaymentDTO > getMonthlyPaymentByAttributeAndValue( @RequestParam( "attribute" ) String attribute, @RequestParam( "valueAttribute" ) String valueAttribute ) {

        return monthlyPaymentService.findMonthlyPaymentByAttributeAndValue( attribute, valueAttribute );
    }

    @PostMapping
    public MonthlyPaymentDTO createMonthlyPayment( @RequestBody MonthlyPaymentRequest monthlyPaymentRequest ) {

        return monthlyPaymentService.createMonthlyPayment( monthlyPaymentRequest );
    }

}