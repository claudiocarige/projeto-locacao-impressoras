package br.com.copyimagem.ms_help_desk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsHelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHelpDeskApplication.class, args);
	}

}
