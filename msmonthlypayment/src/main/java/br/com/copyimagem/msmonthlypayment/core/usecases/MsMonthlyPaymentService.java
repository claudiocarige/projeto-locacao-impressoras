package br.com.copyimagem.msmonthlypayment.core.usecases;

import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;

import java.util.List;


public interface MsMonthlyPaymentService {

    MonthlyPaymentDTO createMonthlyPayment( MonthlyPaymentRequest monthlyPaymentRequest );

    MonthlyPaymentDTO findMonthlyPaymentById( Long id );

    List< MonthlyPaymentDTO > findAllMonthlyPaymentsByCustomerId( Long customerId );

    List< MonthlyPaymentDTO > findMonthlyPaymentByAttributeAndValue( String attribute, String valueAttribute );

}