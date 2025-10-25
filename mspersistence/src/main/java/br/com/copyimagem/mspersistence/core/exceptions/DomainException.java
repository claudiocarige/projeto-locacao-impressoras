package br.com.copyimagem.mspersistence.core.exceptions;

public class DomainException extends RuntimeException {

    private DomainException( String message ) {
        super( message );
    }

    public static DomainException with( String message ) {
        return new DomainException( message );
    }

}
