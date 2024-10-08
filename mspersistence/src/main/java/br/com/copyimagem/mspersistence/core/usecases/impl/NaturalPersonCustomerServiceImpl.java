package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Address;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.NaturalPersonCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.DataIntegrityViolationException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.ConvertObjectToObjectDTOService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.AddressRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerContractRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.NaturalPersonCustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Log4j2
@Service
public class NaturalPersonCustomerServiceImpl implements NaturalPersonCustomerService {

    private final NaturalPersonCustomerRepository naturalPersonCustomerRepository;

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final CustomerContractRepository customerContractRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Autowired
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
        return convertObjectToObjectDTOService.convertEntityAndDTOList( custumerList, NaturalPersonCustomerDTO.class );
    }

    @Override
    public NaturalPersonCustomerDTO findNaturalPersonCustomerById( Long id ) {

        log.info( "[ INFO ] Finding customer by id: {}", id );
        NaturalPersonCustomer naturalPersonCustomer = naturalPersonCustomerRepository.findById( id )
                                            .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        return convertObjectToObjectDTOService.convertToEntityOrDTO(
                                                             naturalPersonCustomer, NaturalPersonCustomerDTO.class );
    }

    @Transactional
    @Override
    public NaturalPersonCustomerDTO saveNaturalPersonCustomer( NaturalPersonCustomerDTO naturalPersonCustomerDTO ) {

        log.info( "[ INFO ] Saving customer. {}", naturalPersonCustomerDTO.getClass() );
                                                                             naturalPersonCustomerDTO.setId( null );
        addressRepository.save( naturalPersonCustomerDTO.getAddress() );
        saveAddress( naturalPersonCustomerDTO );
        generateCustomerContract( naturalPersonCustomerDTO );
        existsCpfOrEmail( naturalPersonCustomerDTO );
        NaturalPersonCustomer savedNaturalCustomer = naturalPersonCustomerRepository
                                        .save( convertObjectToObjectDTOService.convertToEntityOrDTO(
                                                          naturalPersonCustomerDTO, NaturalPersonCustomer.class ) );
        return convertObjectToObjectDTOService.convertToEntityOrDTO(
                                                             savedNaturalCustomer, NaturalPersonCustomerDTO.class );
    }

    @Override
    public CustomerResponseDTO findByCpf( String cpf ) {

        Optional< NaturalPersonCustomer > naturalPersonCustomer = naturalPersonCustomerRepository.findByCpf( cpf );
        if( naturalPersonCustomer.isEmpty() ) {
            log.error( "[ ERROR ] Exception :  {}.", NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToEntityOrDTO(
                                                            naturalPersonCustomer.get(), CustomerResponseDTO.class );
    }

    private void existsCpfOrEmail( NaturalPersonCustomerDTO naturalPersonCustomerDTO ) {

        if( customerRepository.existsCustomerByPrimaryEmail( naturalPersonCustomerDTO.getPrimaryEmail() ) ) {
            log.error( "[ ERROR ] Exception : Email already exists! : {}.", DataIntegrityViolationException.class );
            throw new DataIntegrityViolationException( "Email already exists!" );
        } else if( naturalPersonCustomerRepository
                                        .existsNaturalPersonCustomerByCpf( naturalPersonCustomerDTO.getCpf() ) ) {
            log.error( "[ ERROR ] Exception : CPF already exists! : {}.", DataIntegrityViolationException.class );
            throw new DataIntegrityViolationException( "CPF already exists!" );
        }
    }

    private void generateCustomerContract( NaturalPersonCustomerDTO naturalPersonCustomerDTO ) {

        if( naturalPersonCustomerDTO.getCustomerContract() == null ) {
            CustomerContract contract = new CustomerContract();
            naturalPersonCustomerDTO.setCustomerContract( contract );
            customerContractRepository.save( contract );
        }
    }

    private void saveAddress( NaturalPersonCustomerDTO naturalPersonCustomerDTO ) {

        if( naturalPersonCustomerDTO.getAddress() != null ) {
            naturalPersonCustomerDTO.setAddress( addressRepository.save( naturalPersonCustomerDTO.getAddress() ) );
        } else {
            throw new DataIntegrityViolationException( "Address is null!" );
        }
    }

}
