package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.CustomerContract;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerContractRepository extends JpaRepository< CustomerContract, Long > {
}
