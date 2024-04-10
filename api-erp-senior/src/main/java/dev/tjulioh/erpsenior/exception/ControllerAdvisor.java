package dev.tjulioh.erpsenior.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(ConstraintViolationException e) {

        return bodyException(
                String.format("Houve um problema ao validar o objeto enviado: %s", e.getMessage()),
                e.getLocalizedMessage(),
                e.getConstraintViolations().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        return bodyException(
                String.format("Objeto enviado invalido: %s", e.getMessage()),
                e.getLocalizedMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleException(NullPointerException e) {
        return bodyException(
                String.format("Objeto nao pode ser nulo: %s", e.getMessage()),
                e.getLocalizedMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return bodyException(
                String.format("O argumento deve ser valido: %s", e.getMessage()),
                e.getLocalizedMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public ResponseEntity<Object> bodyException(String mensagem, String causa, String rastro, HttpStatus status) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("dataHora", LocalDateTime.now());
        body.put("mensagem", mensagem);
        body.put("causa", causa);
        body.put("rastro", rastro);

        return new ResponseEntity<>(body, status);
    }
}