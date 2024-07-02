package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;

import java.util.List;


public class NaturalPersonCustomerServiceImpl implements NaturalPersonCustomerService {

    @Override
    public List< NaturalPersonCustomerDTO > findAllNaturalPersonCustomer() {

        return List.of();
    }

    @Override
    public NaturalPersonCustomerDTO findNaturalPersonCustomerById( Long id ) {

        return null;
    }

    @Override
    public NaturalPersonCustomerDTO saveNaturalPersonCustomer( NaturalPersonCustomerDTO naturalPersonCustomer ) {

        return null;
    }

    @Override
    public CustomerResponseDTO findByCpf( String cpf ) {

        return null;
    }

}
