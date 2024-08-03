package br.com.copyimagem.msmonthlypayment.infra.controllers;


import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import br.com.copyimagem.msmonthlypayment.core.usecases.MsMonthlyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping( "/api/v1/monthlypayment-service" )
public class MsMonthlyPaymentController {

    private final MsMonthlyPaymentService monthlyPaymentService;

    @Autowired
    public MsMonthlyPaymentController( MsMonthlyPaymentService monthlyPaymentService ) {

        this.monthlyPaymentService = monthlyPaymentService;
    }

    @GetMapping
    public ResponseEntity< List< MonthlyPaymentDTO > > getAllMonthlyPaymentByCustomerId() {

        return ResponseEntity.ok().body(monthlyPaymentService.findAllMonthlyPaymentsByCustomerId( 1L ));
    }

    @GetMapping( "/search")
    public ResponseEntity<List< MonthlyPaymentDTO >> getMonthlyPaymentByAttributeAndValue( @RequestParam( "attribute" ) String attribute, @RequestParam( "valueAttribute" ) String valueAttribute ) {

        return ResponseEntity.ok().body(monthlyPaymentService.findMonthlyPaymentByAttributeAndValue( attribute, valueAttribute ));
    }

    @PostMapping
    public ResponseEntity<MonthlyPaymentDTO> createMonthlyPayment( @RequestBody MonthlyPaymentRequest monthlyPaymentRequest ) {

        return ResponseEntity.ok().body(monthlyPaymentService.createMonthlyPayment( monthlyPaymentRequest ));
    }
}
