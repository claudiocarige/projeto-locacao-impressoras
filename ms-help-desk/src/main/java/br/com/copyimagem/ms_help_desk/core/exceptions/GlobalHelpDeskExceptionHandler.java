package br.com.copyimagem.ms_help_desk.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Log4j2
@ControllerAdvice
public class GlobalHelpDeskExceptionHandler {

    @ExceptionHandler( NoSuchElementException.class )
    public ResponseEntity< HelpDeskError > noSuchElementException( NoSuchElementException ex,
                                                                   HttpServletRequest request ) {

        HelpDeskError error = new HelpDeskError( System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] NoSuchElementException Class : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( error );
    }

    @ExceptionHandler( IllegalArgumentException.class )
    public ResponseEntity< HelpDeskError > IllegalArgumentException( IllegalArgumentException ex,
                                                                     HttpServletRequest request ) {

        HelpDeskError error = new HelpDeskError( System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
                                                                            ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] IllegalArgumentException Class : %S", error.getMessage() ) );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( error );
    }

    @ExceptionHandler( CustomFeignException.class )
    public ResponseEntity< HelpDeskError > feignExceptionNotFound( CustomFeignException ex,
                                                                  HttpServletRequest request ) {
        var status = ex.getStatus() == 404 ? HttpStatus.NOT_FOUND :
                        ex.getStatus() == 503 ? HttpStatus.SERVICE_UNAVAILABLE: HttpStatus.INTERNAL_SERVER_ERROR;

        HelpDeskError error = new HelpDeskError( System.currentTimeMillis(), ex.getStatus(),
                ex.getMessage(), request.getRequestURI() );
        log.error( String.format( "[ ERROR ] FeignException Class : %S", error.getMessage() ) );
        return ResponseEntity.status( status ).body( error );
    }
}
