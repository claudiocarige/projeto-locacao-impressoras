package br.com.copyimagem.mspersistence.infra.persistence.repositories;

import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MultiPrinterRepository extends JpaRepository< MultiPrinter, Integer> {

    List< MultiPrinter > findAllByCustomerId( Long customerId );

    boolean existsBySerialNumber( String serialNumber );

    @Transactional
    @Modifying
    @Query( "UPDATE MultiPrinter mp SET mp.machineStatus = :status WHERE mp.id = :id" )
    int updateMachineStatusById( @Param( value = "id" ) Integer id, @Param( value = "status" ) MachineStatus status );
}
