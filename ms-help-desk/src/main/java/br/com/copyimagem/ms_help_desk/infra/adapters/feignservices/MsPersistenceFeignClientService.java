package br.com.copyimagem.ms_help_desk.infra.adapters.feignservices;


import br.com.copyimagem.ms_help_desk.core.domain.dtos.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        value = "mspersistence",
        path = "/api/v1"
)
public interface MsPersistenceFeignClientService {

    @GetMapping( value = "/search-client" )
    ResponseEntity< CustomerResponseDTO > searchCustomerByParams(
            @RequestParam( "typeParam" ) String typeParam,
            @RequestParam( "valueParam" ) String valueParam );
}
