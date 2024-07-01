package br.com.copyimagem.mspersistence.core.exceptions;

public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException( String message ) {

        super( message );
    }

}
