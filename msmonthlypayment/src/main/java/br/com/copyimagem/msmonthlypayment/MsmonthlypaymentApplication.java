package br.com.copyimagem.msmonthlypayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsmonthlypaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsmonthlypaymentApplication.class, args);
	}

}
