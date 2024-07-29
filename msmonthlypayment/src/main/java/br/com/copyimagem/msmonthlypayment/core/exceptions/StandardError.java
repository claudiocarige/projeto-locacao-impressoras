package br.com.copyimagem.msmonthlypayment.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandardError {


    private Long timestamp;

    private Integer status;

    private String message;

    private String path;

}

