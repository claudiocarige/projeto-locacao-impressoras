package br.com.copyimagem.mspersistence.infra.controllers;

import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.usecases.interfaces.MultiPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping( "/api/v1/multi-printer" )
public class MultiprinterController {


    private final MultiPrinterService multiPrinterService;

    public MultiprinterController( MultiPrinterService multiPrinterService ) {

        this.multiPrinterService = multiPrinterService;
    }

    @GetMapping
    public ResponseEntity< List< MultiPrinterDTO > > findAllMultiPrinters() {

        return ResponseEntity.ok( multiPrinterService.findAllMultiPrinters() );
    }

}
