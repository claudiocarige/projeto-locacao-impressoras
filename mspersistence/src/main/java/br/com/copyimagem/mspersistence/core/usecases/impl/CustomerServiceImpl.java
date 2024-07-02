package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Override
    public CustomerResponseDTO searchCustomer( String type, String value ) {

        return null;
    }

    @Override
    public List< CustomerResponseDTO > searchAllCustomers() {

        return List.of();
    }

    @Override
    public List< CustomerResponseDTO > searchFinancialSituation( String situation ) {

        return List.of();
    }

    @Override
    public CustomerContract getCustomerContract( Long id ) {

        return null;
    }

    @Override
    public UpdateCustomerDTO updateCustomerAttribute( String attribute, String value, Long id ) {

        return null;
    }

    @Override
    public Customer returnCustomer( Long aLong ) {

        return null;
    }

}
