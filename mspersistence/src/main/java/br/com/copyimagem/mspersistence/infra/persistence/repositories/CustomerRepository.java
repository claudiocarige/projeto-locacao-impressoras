package br.com.copyimagem.mspersistence.infra.persistence.repositories;


import br.com.copyimagem.mspersistence.core.domain.entities.Customer;
import br.com.copyimagem.mspersistence.core.domain.enums.FinancialSituation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository< Customer, Long > {

    Optional< Customer > findByPrimaryEmail( String email );

    List< Customer > findAllByFinancialSituation( FinancialSituation financialSituation );

    Optional< Customer > findByClientName( String valueParam );

    Boolean existsCustomerByPrimaryEmail( String email );

    Optional< Customer > findByPhoneNumber( String phoneNumber );

}
