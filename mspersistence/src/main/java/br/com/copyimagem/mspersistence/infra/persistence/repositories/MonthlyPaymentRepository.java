package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MonthlyPaymentRepository extends JpaRepository< MonthlyPayment, Long > {

    @Query( "SELECT mp FROM MonthlyPayment mp WHERE mp.customer.id = :customerId" )
    List<MonthlyPayment> findAllMonthlyPaymentsByCustomerId( Long customerId );

}
