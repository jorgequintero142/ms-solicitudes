package co.com.crediya.api;

import co.com.crediya.api.dto.RespuestaApi;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private static final String SOLICITUD_CREADA = "Solicitud creada";
    private static final int CODIGO_ESTADO_OK = 200;

    @Operation(
            summary = "Crear un nueva solicitud",
            description = "Crea una nueva solicitud en el sistema",
            tags = {"Solicitudes"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """ 
                                            {
                                              "monto": 100000,
                                              "plazo": 6,
                                              "documentoIdentidad": "444444",
                                              "idTipoPrestamo": 2
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud creada",
                            content = @Content(
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "idSolicitud": 32,
                                                      "monto": 100000,
                                                      "plazo": 6,
                                                      "email": "pedroperez@email.com",
                                                      "documentoIdentidad": "444444",
                                                      "idEstado": 1,
                                                      "idTipoPrestamo": 2
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validación fallida",
                            content = @Content(
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "error": "Validación fallida",
                                                      "message": "No existe tipo de prestamo",
                                                      "status": 400
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public Mono<ServerResponse> registrar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Solicitud.class)
                .flatMap(registrarSolicitudUseCase::registrar)
                .flatMap(solicitudGuardada -> {
                            RespuestaApi<SolicitudCreada> respuesta = new RespuestaApi<>(SolicitudHandler.CODIGO_ESTADO_OK, SolicitudHandler.SOLICITUD_CREADA, solicitudGuardada);
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                        }
                );
    }
}
