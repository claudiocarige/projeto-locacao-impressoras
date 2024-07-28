package br.com.copyimagem.msmonthlypayment.infra.adapters;


import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentDTO;
import br.com.copyimagem.msmonthlypayment.core.domain.representations.MonthlyPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
            value = "ms-persistence-service",
            path = "/api/v1/monthlypayment"
        )
public interface MsPersistenceServiceFeignClient {

    @PostMapping
    MonthlyPaymentDTO createMonthlyPayment( @RequestBody MonthlyPaymentRequest monthlyPaymentRequest );
}
