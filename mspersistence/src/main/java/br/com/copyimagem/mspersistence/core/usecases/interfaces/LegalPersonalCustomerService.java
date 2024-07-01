package br.com.copyimagem.mspersistence.core.usecases.interfaces;

import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;

import java.util.List;


public interface LegalPersonalCustomerService {

    List< LegalPersonalCustomerDTO > findAllLegalPersonalCustomer();

    LegalPersonalCustomerDTO findLegalPersonalCustomerById( Long id );

    LegalPersonalCustomerDTO saveLegalPersonalCustomer( LegalPersonalCustomerDTO legalPersonalCustomer );

    CustomerResponseDTO findByCnpj( String cnpj );

}
