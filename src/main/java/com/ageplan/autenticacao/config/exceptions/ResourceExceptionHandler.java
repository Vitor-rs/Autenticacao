package com.ageplan.autenticacao.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

/**
 * Classe de manipulador de exceções para lidar com exceções específicas da aplicação.
 * Esta classe fornece métodos para tratar exceções e retornar respostas HTTP apropriadas.
 */
@ControllerAdvice
public class ResourceExceptionHandler {

    /**
     * Manipula exceções do tipo ResourceNotFoundException.
     *
     * @param e       a exceção ResourceNotFoundException lançada.
     * @param request o objeto HttpServletRequest associado à solicitação.
     * @return uma ResponseEntity contendo detalhes do erro e o status HTTP NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(
            ResourceNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Recurso não encontrado");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    /**
     * Manipula exceções do tipo DataBaseException.
     *
     * @param e       a exceção DataBaseException lançada.
     * @param request o objeto HttpServletRequest associado à solicitação.
     * @return uma ResponseEntity contendo detalhes do erro e o status HTTP BAD_REQUEST.
     */
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> database(
            DataBaseException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Database exception");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }
}