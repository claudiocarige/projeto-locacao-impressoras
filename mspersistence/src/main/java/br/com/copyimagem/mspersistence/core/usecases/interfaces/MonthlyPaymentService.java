package br.com.copyimagem.mspersistence.core.usecases.interfaces;

import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;


public interface MonthlyPaymentService {
    MonthlyPaymentDTO createMonthlyPayment( MonthlyPaymentRequest monthlyPaymentRequest );

    MonthlyPaymentDTO findMonthlyPaymentById( Long id );

}
