package co.com.crediya.api;

import co.com.crediya.model.solicitud.exceptions.SolicitudException;
import co.com.crediya.usecase.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SolicitudException.class)
    public Mono<ResponseEntity<Map<String, Object>>> manejarErrores(SolicitudException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getError());
        error.put("mensaje", ex.getMessage());
        error.put("estado", ex.getCodigoEstado());
        logger.warn("Un error ha ocurrido {} ", error);
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(ex.getCodigoEstado())).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> manejarErroresGenericos(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", Constantes.ERROR_INESPERADO);
        error.put("mensaje", Constantes.MENSAJE_ERROR_INESPERADO);
        error.put("estado", Constantes.CODIGO_ERROR_INESPERADO);
        logger.error("Un error inesperado ha ocurrido {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(Constantes.CODIGO_ERROR_INESPERADO).body(error));
    }
}
