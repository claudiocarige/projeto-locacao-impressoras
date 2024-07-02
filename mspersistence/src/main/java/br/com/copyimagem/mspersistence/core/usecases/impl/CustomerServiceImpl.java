package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.dtos.CustomerResponseDTO;
import br.com.copyimagem.mspersistence.core.dtos.UpdateCustomerDTO;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    private final LegalPersonalCustomerService legalPersonalCustomerService;

    private final NaturalPersonCustomerService naturalPersonCustomerService;

    public CustomerServiceImpl( CustomerRepository customerRepository,
                                ConvertObjectToObjectDTOService convertObjectToObjectDTOService,
                                LegalPersonalCustomerServiceImpl legalPersonalCustomerService,
                                NaturalPersonCustomerServiceImpl naturalPersonCustomerService ) {

        this.customerRepository = customerRepository;
        this.convertObjectToObjectDTOService = convertObjectToObjectDTOService;
        this.legalPersonalCustomerService = legalPersonalCustomerService;
        this.naturalPersonCustomerService = naturalPersonCustomerService;
    }

    @Override
    public CustomerResponseDTO searchCustomer( String typeParam, String valueParam ) {

        return
                switch( typeParam.toLowerCase() ) {
                    case "id" -> findById( Long.parseLong( valueParam ) );
                    case "cpf" -> findByCpf( valueParam );
                    case "cnpj" -> findByCnpj( valueParam );
                    case "email" -> findByPrimaryEmail( valueParam );
                    case "clientname" -> findByClientName( valueParam );
                    case "phonenumber" -> findByPhoneNumber( valueParam );
                    default ->
                            throw new IllegalArgumentException( "Parameter [ " + typeParam + " ] type not accepted." );
                };
    }

    @Override
    public List< CustomerResponseDTO > searchAllCustomers() {

        List< Customer > customerList = customerRepository.findAll();
        return customerList.stream()
                .map( convertObjectToObjectDTOService::convertToCustomerResponseDTO ).toList();
    }

    @Override
    public List< CustomerResponseDTO > searchFinancialSituation( String situation ) {

        return List.of();
    }

    @Override
    public CustomerContract getCustomerContract( Long id ) {

        Customer customer = customerRepository.findById( id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        return customer.getCustomerContract();
    }

    @Override
    public UpdateCustomerDTO updateCustomerAttribute( String attribute, String value, Long id ) {

        return null;
    }

    @Override
    public Customer returnCustomer( Long aLong ) {

        return null;
    }

    private CustomerResponseDTO findById( Long id ) {

        Optional< Customer > customerOptional = customerRepository.findById( id );
        if( customerOptional.isEmpty() ) {
            log.error( "[ ERROR ] Exception (findById() method in CustomerServiceImpl class):  {}.",
                    NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToCustomerResponseDTO( customerOptional.get() );
    }

    private CustomerResponseDTO findByCpf( String valueParam ) {

        return naturalPersonCustomerService.findByCpf( valueParam );
    }

    private CustomerResponseDTO findByCnpj( String valueParam ) {

        return legalPersonalCustomerService.findByCnpj( valueParam );
    }

    private CustomerResponseDTO findByPrimaryEmail( String email ) {

        Optional< Customer > customerOptional = customerRepository.findByPrimaryEmail( email );
        if( customerOptional.isEmpty() ) {
            log.error( "[ ERROR ] Exception (findByPrimaryEmail() method in CustomerServiceImpl class) :  {}.",
                    NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToCustomerResponseDTO( customerOptional.get() );
    }

    private CustomerResponseDTO findByClientName( String valueParam ) {

        return customerRepository.findByClientName( valueParam )
                .map( convertObjectToObjectDTOService::convertToCustomerResponseDTO )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
    }

    private CustomerResponseDTO findByPhoneNumber( String phoneNumber ) {

        Optional< Customer > customerOptional = customerRepository.findByPhoneNumber( phoneNumber );
        if( customerOptional.isEmpty() ) {
            log.error( "[ ERROR ] Exception (findByPhoneNumber() method in CustomerServiceImpl class) :  {}.",
                    NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToCustomerResponseDTO( customerOptional.get() );
    }

}
