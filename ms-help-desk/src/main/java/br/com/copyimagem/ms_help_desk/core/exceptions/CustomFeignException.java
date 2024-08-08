package br.com.copyimagem.ms_help_desk.core.exceptions;

import lombok.Getter;


@Getter
public class CustomFeignException extends RuntimeException {

    private int status;

    public CustomFeignException( String message, int status ) {

        super( message );
        this.status = status;

    }

    public CustomFeignException( String message ) {

        super( message );
    }

}
