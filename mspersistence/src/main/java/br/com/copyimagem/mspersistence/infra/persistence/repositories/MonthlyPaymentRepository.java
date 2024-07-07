package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.MonthlyPayment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MonthlyPaymentRepository extends JpaRepository< MonthlyPayment, Long > {
}
