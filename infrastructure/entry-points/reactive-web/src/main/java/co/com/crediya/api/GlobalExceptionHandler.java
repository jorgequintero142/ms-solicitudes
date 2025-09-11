package co.com.crediya.api;

import co.com.crediya.model.solicitud.exceptions.SolicitudException;
import co.com.crediya.usecase.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private static final String PROPIEDAD_ERROR = "error";
    private static final String PROPIEDAD_MENSAJE = "mensaje";
    private static final String PROPIEDAD_ESTADO = "estado";
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> error = new HashMap<>();
        HttpStatus status = HttpStatus.valueOf(Constantes.CODIGO_ERROR_INESPERADO);

        if (ex instanceof SolicitudException solicitudException) {
            status = HttpStatus.valueOf(solicitudException.getCodigoEstado());
            error.put(PROPIEDAD_ERROR, solicitudException.getError());
            error.put(PROPIEDAD_MENSAJE, solicitudException.getMessage());
            error.put(PROPIEDAD_ESTADO, solicitudException.getCodigoEstado());
            logger.warn("Error de autenticación: {}", error);
        } else {
            error.put(PROPIEDAD_ERROR, Constantes.ERROR_INESPERADO);
            error.put(PROPIEDAD_MENSAJE, Constantes.MENSAJE_ERROR_INESPERADO);
            error.put(PROPIEDAD_ESTADO, Constantes.CODIGO_ERROR_INESPERADO);
            logger.error("Error inesperado: {}", ex.getMessage(), ex);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                """
                        {
                          "error": "%s",
                          "mensaje": "%s",
                          "estado": "%s"
                        }
                        """,
                error.get(PROPIEDAD_ERROR),
                error.get(PROPIEDAD_MENSAJE),
                error.get(PROPIEDAD_ESTADO)
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }
}
