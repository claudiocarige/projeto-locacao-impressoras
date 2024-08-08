package br.com.copyimagem.ms_help_desk.core.domain.dtos;

import org.springframework.http.ResponseEntity;


public record ResultFeignClient( ResponseEntity< UserRequestDTO> client,
                                 ResponseEntity< UserRequestDTO> technical) { }
