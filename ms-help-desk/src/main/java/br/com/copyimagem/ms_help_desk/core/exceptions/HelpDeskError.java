package br.com.copyimagem.ms_help_desk.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpDeskError {


    private Long timestamp;

    private Integer status;

    private String message;

    private String path;

}