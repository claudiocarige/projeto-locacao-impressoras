package br.com.copyimagem.msmonthlypayment.infra.adapters.feingservices;


import br.com.copyimagem.msmonthlypayment.core.domain.representations.CustomerContractDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MultiPrinterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(
            value = "mspersistence",
            path = "/api/v1"
        )
public interface MsPersistenceServiceFeignClient {

    @GetMapping( value = "/customers/search-contract/{customerId}" )
    CustomerContractDTO searchCustomerContract( @PathVariable Long customerId );

    @GetMapping( "/multi-printer/customer/{customerId}" )
    List< MultiPrinterDTO > findAllMultiPrintersByCustomerId( @PathVariable Long customerId );


}
