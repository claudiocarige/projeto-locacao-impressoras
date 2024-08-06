package br.com.copyimagem.ms_help_desk.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;


@Log4j2
@ControllerAdvice
public class GlobalHelpDeskExceptionHandler {

    public ResponseEntity< HelpDeskError > noSuchElementException( NoSuchElementException ex,
                                                                   HttpServletRequest request ) {

        HelpDeskError error = new HelpDeskError( System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] NoSuchElementException Class : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( error );
    }

    public ResponseEntity< HelpDeskError > IllegalArgumentException( IllegalArgumentException ex,
                                                                     HttpServletRequest request ) {

        HelpDeskError error = new HelpDeskError( System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] IllegalArgumentException Class : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( error );
    }
}
