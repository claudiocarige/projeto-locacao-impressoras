package br.com.copyimagem.mspersistence.core.usecases.impl;

import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import br.com.copyimagem.mspersistence.core.domain.entities.LegalPersonalCustomer;
import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import br.com.copyimagem.mspersistence.core.domain.enums.FinancialSituation;
import br.com.copyimagem.mspersistence.core.dtos.*;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.ConvertObjectToObjectDTOService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.CustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.LegalPersonalCustomerService;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.NaturalPersonCustomerService;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    private final LegalPersonalCustomerService legalPersonalCustomerService;

    private final NaturalPersonCustomerService naturalPersonCustomerService;

    @Autowired
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
        return convertObjectToObjectDTOService.convertEntityAndDTOList(customerList, CustomerResponseDTO.class);
    }

    @Override
    public List< CustomerResponseDTO > searchFinancialSituation( String situation ) {

        FinancialSituation financialSituation = FinancialSituation.valueOf( situation );
        List< Customer > customerList = customerRepository.findAllByFinancialSituation( financialSituation );
        return convertObjectToObjectDTOService.convertEntityAndDTOList(customerList, CustomerResponseDTO.class);
    }

    @Override
    public CustomerContract getCustomerContract( Long id ) {

        Customer customer = customerRepository.findById( id )
                                             .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        return customer.getCustomerContract();
    }

    @Override
    public UpdateCustomerDTO updateCustomerAttribute( String attribute, String value, Long id ) {

        log.info( "[ INFO ] Updating Customer attribute : {}", attribute );
        isNotNull( attribute, value );
        Customer customer = customerRepository.findById( id )
                                             .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        isContainsAnyOfTheAttributes( attribute );
        needsValidations( attribute, value );
        return getUpdateCustomerAttribute( attribute, value, customer );
    }

    @Override
    public Customer returnCustomer( Long id ) {

        return customerRepository.findById( id )
                .orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
    }

    @Override
    public CustomerContractDTO findCustomerContractByCustomerId( Long customerId ) {

        CustomerContract customerContract = customerRepository.findCustomerContractByCustomerId( customerId );
        return new CustomerContractDTO(
                customerContract.getId(),
                customerContract.getPrintingFranchisePB(), customerContract.getPrintingFranchiseColor(),
                customerContract.getPrinterTypePB().getRate(), customerContract.getPrinterTypeColor().getRate());
    }

    private CustomerResponseDTO findById( Long id ) {

        Optional< Customer > customerOptional = customerRepository.findById( id );
        if( customerOptional.isEmpty() ) {
            log.error( "[ ERROR ] Exception (findById() method in CustomerServiceImpl class):  {}.",
                                                                                        NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToEntityOrDTO( customerOptional.get(), CustomerResponseDTO.class );
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
        return convertObjectToObjectDTOService.convertToEntityOrDTO( customerOptional.get(), CustomerResponseDTO.class );
    }

    private CustomerResponseDTO findByClientName( String valueParam ) {

        Customer customer = customerRepository.findByClientName( valueParam ).orElseThrow( () -> new NoSuchElementException( "Customer not found" ) );
        return convertObjectToObjectDTOService.convertToEntityOrDTO( customer,
                                                                     CustomerResponseDTO.class );
    }

    private CustomerResponseDTO findByPhoneNumber( String phoneNumber ) {

        Optional< Customer > customerOptional = customerRepository.findByPhoneNumber( phoneNumber );
        if( customerOptional.isEmpty() ) {
            log.error( "[ ERROR ] Exception (findByPhoneNumber() method in CustomerServiceImpl class) :  {}.",
                                                                                         NoSuchElementException.class );
            throw new NoSuchElementException( "Customer not found" );
        }
        return convertObjectToObjectDTOService.convertToEntityOrDTO( customerOptional.get(), CustomerResponseDTO.class );
    }

    private void isNotNull( String attribute, String value ) {

        String[] attributes = {"cpf", "cnpj", "primaryEmail", "phoneNumber", "clientName", "whatsapp"};
        for( String attribute1 : attributes ) {
            if( attribute1.equals( attribute ) ) {
                if( value == null ) {
                    throw new IllegalArgumentException( attribute.toUpperCase() + " cannot be null." );
                }
            }
        }
    }

    private void isContainsAnyOfTheAttributes( String attribute ) {

        if( List.of( "emailList", "multiPrinterList", "monthlyPaymentList" ).contains( attribute ) ) {
            throw new IllegalArgumentException( "This attribute cannot be changed on this endpoint." );
        }
    }

    private void needsValidations( String attribute, String value ) {

        if( List.of( "cpf", "cnpj", "primaryEmail" ).contains( attribute ) ) {
            validateAttribute( attribute, value );
        }
    }

    private void validateAttribute( String attribute, String value ) {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        switch( attribute ) {
            case "cnpj" -> {
                Set< ConstraintViolation< LegalPersonalCustomerDTO > > cnpjViolations = validator
                                                    .validateValue( LegalPersonalCustomerDTO.class, attribute, value );
                if( ! cnpjViolations.isEmpty() ) {
                    throw new IllegalArgumentException( cnpjViolations.iterator().next().getMessage() );
                }
            }
            case "primaryEmail" -> {
                Set< ConstraintViolation< NaturalPersonCustomerDTO > > emailViolations = validator
                                                    .validateValue( NaturalPersonCustomerDTO.class, attribute, value );
                log.info( "[ INFO ] emailViolations : {}", emailViolations );
                if( ! emailViolations.isEmpty() ) {
                    throw new IllegalArgumentException( emailViolations.iterator().next().getMessage() );
                }
            }
            case "cpf" -> {
                Set< ConstraintViolation< NaturalPersonCustomerDTO > > cpfViolations = validator
                                                    .validateValue( NaturalPersonCustomerDTO.class, attribute, value );
                if( ! cpfViolations.isEmpty() ) {
                    throw new IllegalArgumentException( cpfViolations.iterator().next().getMessage() );
                }
            }
        }
    }

    private UpdateCustomerDTO getUpdateCustomerAttribute( String attribute, String value, Customer customer ) {

        switch( attribute ) {
            case "cpf" -> {
                NaturalPersonCustomer naturalPersonCustomer = ( NaturalPersonCustomer ) customer;
                naturalPersonCustomer.setCpf( value );
                return convertObjectToObjectDTOService.convertToEntityOrDTO(
                                          customerRepository.save( naturalPersonCustomer ), UpdateCustomerDTO.class );
            }
            case "cnpj" -> {
                LegalPersonalCustomer legalPersonalCustomer = ( LegalPersonalCustomer ) customer;
                legalPersonalCustomer.setCnpj( value );
                return convertObjectToObjectDTOService.convertToEntityOrDTO(
                                          customerRepository.save( legalPersonalCustomer ), UpdateCustomerDTO.class );
            }
            case "primaryEmail" -> {
                customer.setPrimaryEmail( value );
            }
            case "phoneNumber" -> {
                customer.setPhoneNumber( value );
            }
            case "clientName" -> {
                customer.setClientName( value );
            }
            case "whatsapp" -> {
                customer.setWhatsapp( value );
            }
            case "bankCode" -> {
                customer.setBankCode( value );
            }
            case "financialSituation" -> {
                customer.setFinancialSituation( FinancialSituation.valueOf( value ) );
            }
            case "payDay" -> {
                customer.setPayDay( Byte.parseByte( value ) );
            }
            default -> throw new IllegalArgumentException( "Attribute not found." );
        }
        return convertObjectToObjectDTOService
                .convertToEntityOrDTO( customerRepository.save( customer ), UpdateCustomerDTO.class );
    }

}
