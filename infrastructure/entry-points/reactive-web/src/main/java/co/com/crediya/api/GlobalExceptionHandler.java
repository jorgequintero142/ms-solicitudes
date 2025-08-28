package co.com.crediya.api;

import co.com.crediya.model.solicitud.exceptions.NoEncontradoException;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ParametroNoValidoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> manejar(ParametroNoValidoException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Validación fallida");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(NoEncontradoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericError(NoEncontradoException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
}
