package org.springframework.samples.petclinic.system;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  // General unhandled exceptions
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> genericExceptionHandler(Exception ex, WebRequest request) {

    log.error("Unexpected service error: " + ex.getMessage(), ex);

    return Optional.of(
            createProblemDetail(
                ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, null, request))
        .map(
            pblm ->
                handleExceptionInternal(
                    ex, pblm, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request))
        .orElseThrow();
  }
}
