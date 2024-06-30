package br.com.copyimagem.mspersistence.core.usecases.interfaces;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;

import java.util.List;


public interface CustomerService {


    CustomerResponseDTO searchCustomer( String type, String value );

    List< CustomerResponseDTO > searchAllCustomers();

    List< CustomerResponseDTO > searchFinancialSituation( String situation );

    CustomerContract getCustomerContract( Long id );

    UpdateCustomerDTO updateCustomerAttribute( String attribute, String value, Long id );

    Customer returnCustomer( Long aLong );

}
