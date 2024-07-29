package br.com.copyimagem.msmonthlypayment.core.usecases.impl;

import br.com.copyimagem.msmonthlypayment.core.domain.entities.MonthlyPayment;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class ConvertObjectToObjectDTOService {


    private final ModelMapper modelMapper;

    public ConvertObjectToObjectDTOService( ModelMapper modelMapper ) {

        this.modelMapper = modelMapper;
    }

    public MonthlyPaymentDTO convertToMonthlyPaymentDTO( MonthlyPayment monthlyPayment ) {

        return modelMapper.map( monthlyPayment, MonthlyPaymentDTO.class );
    }

}
