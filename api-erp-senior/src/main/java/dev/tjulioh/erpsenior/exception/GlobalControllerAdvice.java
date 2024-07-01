package dev.tjulioh.erpsenior.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(ConstraintViolationException e) {

        return bodyException(
                "Houve um problema ao validar o objeto enviado",
                e.getLocalizedMessage(),
                e.getConstraintViolations().toString(),
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        return bodyException(
                "Objeto enviado invalido",
                e.getLocalizedMessage(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleException(NullPointerException e) {
        return bodyException(
                "Objeto nao pode ser nulo",
                e.getLocalizedMessage(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return bodyException(
                "O argumento deve ser valido",
                e.getLocalizedMessage(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleException(NotFoundException e) {
        return bodyException(
                "A entidade nao foi encontrada",
                e.getLocalizedMessage(),
                Arrays.toString(e.getStackTrace()),
                HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<Object> bodyException(String mensagem, String causa, String processo, HttpStatus status) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("dataHora", LocalDateTime.now());
        body.put("mensagem", mensagem);
        body.put("causa", causa);
        body.put("processo", processo);

        return new ResponseEntity<>(body, status);
    }
}