package br.com.copyimagem.mspersistence.core.usecases.interfaces;

import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentDTO;
import br.com.copyimagem.mspersistence.core.dtos.MonthlyPaymentRequest;

import java.util.List;


public interface MonthlyPaymentService {
    MonthlyPaymentDTO createMonthlyPayment( MonthlyPaymentRequest monthlyPaymentRequest );

    MonthlyPaymentDTO findMonthlyPaymentById( Long id );

    List<MonthlyPaymentDTO> findAllMonthlyPaymentsByCustomerId( Long customerId );

}
