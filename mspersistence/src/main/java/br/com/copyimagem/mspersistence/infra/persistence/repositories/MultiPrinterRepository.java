package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MultiPrinterRepository extends JpaRepository< MultiPrinter, Integer> {

    List< MultiPrinter > findAllByCustomerId( Long customerId );
}
