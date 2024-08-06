package br.com.copyimagem.ms_help_desk.infra.adapters.feignservices;


import br.com.copyimagem.ms_help_desk.core.domain.dtos.UserRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        value = "mspersistence",
        path = "/api/v1/customers"
)
public interface MsPersistenceFeignClientService {

    @GetMapping( value = "/search-client" )
    ResponseEntity< UserRequestDTO > searchCustomerByParams(
            @RequestParam( "typeParam" ) String typeParam,
            @RequestParam( "valueParam" ) String valueParam );
}
