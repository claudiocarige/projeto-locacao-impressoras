package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MultiPrinterRepository extends JpaRepository< MultiPrinter, Integer> {
}
