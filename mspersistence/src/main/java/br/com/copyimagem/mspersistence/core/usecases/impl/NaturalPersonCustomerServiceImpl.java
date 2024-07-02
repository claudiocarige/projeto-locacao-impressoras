package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerContractRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.NaturalPersonCustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class NaturalPersonCustomerServiceImpl implements NaturalPersonCustomerService {

    private final NaturalPersonCustomerRepository naturalPersonCustomerRepository;

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final CustomerContractRepository customerContractRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    public NaturalPersonCustomerServiceImpl( NaturalPersonCustomerRepository naturalPersonCustomerRepository,
                                             CustomerRepository customerRepository,
                                             AddressRepository addressRepository,
                                             CustomerContractRepository customerContractRepository,
                                             ConvertObjectToObjectDTOService convertObjectToObjectDTOService ) {

        this.naturalPersonCustomerRepository = naturalPersonCustomerRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerContractRepository = customerContractRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
    }

    @Override
    public List< NaturalPersonCustomerDTO > findAllNaturalPersonCustomer() {

        log.info( "[ INFO ] Finding all customers" );
        List< NaturalPersonCustomer > custumerList = naturalPersonCustomerRepository.findAll();
        return custumerList.stream()
                                .map( convertObjectToObjectDTOService::convertToNaturalPersonCustomerDTO ).toList();
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
