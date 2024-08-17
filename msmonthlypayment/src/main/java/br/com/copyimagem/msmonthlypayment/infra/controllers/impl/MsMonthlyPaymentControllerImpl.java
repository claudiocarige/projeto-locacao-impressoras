package br.com.copyimagem.msmonthlypayment.infra.controllers.impl;


import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import br.com.copyimagem.msmonthlypayment.core.usecases.MsMonthlyPaymentService;
import br.com.copyimagem.msmonthlypayment.infra.controllers.MsMonthlyPaymentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MsMonthlyPaymentControllerImpl implements MsMonthlyPaymentController {

    private final MsMonthlyPaymentService monthlyPaymentService;

    @Autowired
    public MsMonthlyPaymentControllerImpl( MsMonthlyPaymentService monthlyPaymentService ) {

        this.monthlyPaymentService = monthlyPaymentService;
    }

    @Override
    public ResponseEntity< List< MonthlyPaymentDTO > > getAllMonthlyPaymentByCustomerId() {

        return ResponseEntity.ok().body( monthlyPaymentService.findAllMonthlyPaymentsByCustomerId( 1L ) );
    }

    @Override
    public ResponseEntity< List< MonthlyPaymentDTO > > getMonthlyPaymentByAttributeAndValue(
            @RequestParam( "attribute" ) String attribute, @RequestParam( "valueAttribute" ) String valueAttribute ) {

        return ResponseEntity.ok().body( monthlyPaymentService.
                                                 findMonthlyPaymentByAttributeAndValue( attribute, valueAttribute ) );
    }

    @Override
    public ResponseEntity< MonthlyPaymentDTO > createMonthlyPayment(
                                                          @RequestBody MonthlyPaymentRequest monthlyPaymentRequest ) {

        return ResponseEntity.ok().body( monthlyPaymentService.createMonthlyPayment( monthlyPaymentRequest ) );
    }

}
