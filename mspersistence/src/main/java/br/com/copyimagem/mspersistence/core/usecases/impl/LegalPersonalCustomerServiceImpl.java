package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerContractRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.LegalPersonalCustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class LegalPersonalCustomerServiceImpl implements LegalPersonalCustomerService{

    private final LegalPersonalCustomerRepository legalPersonalCustomerRepository;

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final CustomerContractRepository customerContractRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    public LegalPersonalCustomerServiceImpl(
            LegalPersonalCustomerRepository legalPersonalCustomerRepository,
            CustomerRepository customerRepository, AddressRepository addressRepository,
            CustomerContractRepository customerContractRepository,
            ConvertObjectToObjectDTOService convertObjectToObjectDTOService ) {

        this.legalPersonalCustomerRepository = legalPersonalCustomerRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerContractRepository = customerContractRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
    }

    @Override
    public List< LegalPersonalCustomerDTO > findAllLegalPersonalCustomer() {

        log.info( "[ INFO ] Finding all LegalPersonalCustomers." );
        List< LegalPersonalCustomer > legalPersonalCustumerList = legalPersonalCustomerRepository.findAll();
        return legalPersonalCustumerList.stream()
                .map( convertObjectToObjectDTOService::convertToLegalPersonalCustomerDTO ).toList();
    }

    @Override
    public LegalPersonalCustomerDTO findLegalPersonalCustomerById( Long id ) {

        log.info( "[ INFO ] Finding LegalPersonalCustomer by id: {}", id );
        LegalPersonalCustomer legalPersonalCustomer = legalPersonalCustomerRepository.findById( id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        LegalPersonalCustomerDTO legalPersonalCustomerDTO =
                convertObjectToObjectDTOService.convertToLegalPersonalCustomerDTO( legalPersonalCustomer );
        legalPersonalCustomerDTO.setMultiPrinterList( legalPersonalCustomer
                .getMultiPrinterList().stream()
                .map( convertObjectToObjectDTOService::convertToMultiPrinterDTO )
                .collect( Collectors.toList() ) );
        return legalPersonalCustomerDTO;
    }

    @Override
    public LegalPersonalCustomerDTO saveLegalPersonalCustomer( LegalPersonalCustomerDTO legalPersonalCustomer ) {

        return null;
    }

    @Override
    public CustomerResponseDTO findByCnpj( String cnpj ) {

        return null;
    }

}
