package br.com.copyimagem.ms_help_desk.infra.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMaperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
