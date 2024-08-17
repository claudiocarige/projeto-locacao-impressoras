package br.com.copyimagem.msmonthlypayment.infra.controllers;

import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping( "/api/v1/monthlypayment-service" )
public interface MsMonthlyPaymentController {

    @GetMapping
    ResponseEntity< List< MonthlyPaymentDTO > > getAllMonthlyPaymentByCustomerId();

    @GetMapping( "/search" )
    ResponseEntity< List< MonthlyPaymentDTO > > getMonthlyPaymentByAttributeAndValue(
            @RequestParam( "attribute" ) String attribute, @RequestParam( "valueAttribute" ) String valueAttribute );

    @PostMapping
    ResponseEntity< MonthlyPaymentDTO > createMonthlyPayment(
                                                          @RequestBody MonthlyPaymentRequest monthlyPaymentRequest );

}
