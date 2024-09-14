package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.LegalPersonalCustomerDTO;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.DataIntegrityViolationException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.ConvertObjectToObjectDTOService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerContractRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.LegalPersonalCustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Log4j2
@Service
public class LegalPersonalCustomerServiceImpl implements LegalPersonalCustomerService {

    private final LegalPersonalCustomerRepository legalPersonalCustomerRepository;

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final CustomerContractRepository customerContractRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Autowired
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
        return convertObjectToObjectDTOService.convertEntityAndDTOList( legalPersonalCustumerList, LegalPersonalCustomerDTO.class );
    }

    @Override
    public LegalPersonalCustomerDTO findLegalPersonalCustomerById( Long id ) {

        log.info( "[ INFO ] Finding LegalPersonalCustomer by id: {}", id );
        LegalPersonalCustomer legalPersonalCustomer = legalPersonalCustomerRepository.findById( id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        LegalPersonalCustomerDTO legalPersonalCustomerDTO =
                convertObjectToObjectDTOService.convertToEntityOrDTO( legalPersonalCustomer, LegalPersonalCustomerDTO.class );
        legalPersonalCustomerDTO.setMultiPrinterList( convertObjectToObjectDTOService.convertEntityAndDTOList(
                                                legalPersonalCustomer.getMultiPrinterList(), MultiPrinterDTO.class ) );
        return legalPersonalCustomerDTO;
    }

    @Transactional
    @Override
    public LegalPersonalCustomerDTO saveLegalPersonalCustomer( LegalPersonalCustomerDTO legalPersonalCustomerDTO ) {

        log.info( "[ INFO ] Saving LegalPersonalCustomer" );
        legalPersonalCustomerDTO.setId( null );
        saveAddress( legalPersonalCustomerDTO );
        existsCnpjOrEmail( legalPersonalCustomerDTO );
        generateCustomerContract( legalPersonalCustomerDTO );
        LegalPersonalCustomer saveLegalPersonalCustomer = legalPersonalCustomerRepository
                .save( convertObjectToObjectDTOService.convertToEntityOrDTO( legalPersonalCustomerDTO, LegalPersonalCustomer.class ) );
        return convertObjectToObjectDTOService.convertToEntityOrDTO( saveLegalPersonalCustomer, LegalPersonalCustomerDTO.class );
    }

    @Override
    public CustomerResponseDTO findByCnpj( String cnpj ) {

        log.info( "[ INFO ] Finding LegalPersonalCustomer by CNPJ." );
        Optional< LegalPersonalCustomer > legalPersonalCustomer = legalPersonalCustomerRepository.findByCnpj( cnpj );
        if( legalPersonalCustomer.isEmpty() ) {
            log.error( "[ ERROR ] Exception : Customer not found :  {}.", NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToEntityOrDTO( legalPersonalCustomer.get(), CustomerResponseDTO.class );
    }

    private void existsCnpjOrEmail( LegalPersonalCustomerDTO legalPersonalCustomerDTO ) {

        if( customerRepository.existsCustomerByPrimaryEmail( legalPersonalCustomerDTO.getPrimaryEmail() ) ) {
            log.error( "[ ERROR ] Exception : Email already exists! : {}.", customerRepository
                                        .existsCustomerByPrimaryEmail( legalPersonalCustomerDTO.getPrimaryEmail() ) );
            throw new DataIntegrityViolationException( "Email already exists!" );
        } else if( legalPersonalCustomerRepository
                                            .existsLegalPersonalCustomerByCnpj( legalPersonalCustomerDTO.getCnpj() ) ) {
            log.error( "[ ERROR ] Exception : CNPJ already exists! : {}.",
                                                                                DataIntegrityViolationException.class );
            throw new DataIntegrityViolationException( "CNPJ already exists!" );
        }
    }

    private void generateCustomerContract( LegalPersonalCustomerDTO legalPersonalCustomerDTO ) {

        if( legalPersonalCustomerDTO.getCustomerContract() == null ) {
            CustomerContract contract = new CustomerContract();
            CustomerContract savedContract = customerContractRepository.save( contract );
            legalPersonalCustomerDTO.setCustomerContract( savedContract );
        }
    }

    private void saveAddress( LegalPersonalCustomerDTO legalPersonalCustomerDTO ) {

        if( legalPersonalCustomerDTO.getAddress() != null ) {
            legalPersonalCustomerDTO.setAddress( addressRepository.save( legalPersonalCustomerDTO.getAddress() ) );
        } else {
            throw new DataIntegrityViolationException( "Address is null!" );
        }
    }

}
