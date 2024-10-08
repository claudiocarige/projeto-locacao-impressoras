package br.com.copyimagem.mspersistence.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler( NoSuchElementException.class )
    public ResponseEntity< StandardError > noSuchElementException( NoSuchElementException ex,
                                                                   HttpServletRequest request ) {

        StandardError error = new StandardError( System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] NoSuchElementException Classs : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( error );
    }

    @ExceptionHandler( DataIntegrityViolationException.class )
    public ResponseEntity< StandardError > dataIntegrityViolationException( DataIntegrityViolationException ex,
                                                                            HttpServletRequest request ) {

        StandardError error = new StandardError( System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] DataIntegrityViolationException Classs : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( error );
    }

    @ExceptionHandler( IllegalArgumentException.class )
    public ResponseEntity< StandardError > illegalArgumentException( IllegalArgumentException ex,
                                                                     HttpServletRequest request ) {

        StandardError error = new StandardError( System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] IllegalArgumentException Classs : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( error );
    }

}
