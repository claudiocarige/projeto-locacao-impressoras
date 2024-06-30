package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.NaturalPersonCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface NaturalPersonCustomerRepository extends JpaRepository< NaturalPersonCustomer, Long > {


    Optional< NaturalPersonCustomer > findByCpf( String cpf );

}
