package co.com.crediya.api;

import co.com.crediya.model.solicitud.Solicitud;
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
    private  final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @Operation(
            summary = "Crear un nueva solicitud",
            description = "Crea una nueva solicitud en el sistema",
            tags = {"Solicitudes"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                   example = "{\n" +
                                           "  \"monto\": 100000,\n" +
                                           "  \"plazo\": 6,\n" +
                                           "  \"documentoIdentidad\": \"444444\",\n" +
                                           "  \"idTipoPrestamo\": 2\n" +
                                           "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud creada",
                            content = @Content(
                                    schema = @Schema(
                                           example = "{\n" +
                                                   "  \"idSolicitud\": 32,\n" +
                                                   "  \"monto\": 100000,\n" +
                                                   "  \"plazo\": 6,\n" +
                                                   "  \"email\": \"pedroperez@email.com\",\n" +
                                                   "  \"documentoIdentidad\": \"444444\",\n" +
                                                   "  \"idEstado\": 1,\n" +
                                                   "  \"idTipoPrestamo\": 2\n" +
                                                   "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validación fallida",
                            content = @Content(
                            schema = @Schema(
                                    example = "{\n" +
                                            "  \"error\": \"Validación fallida\",\n" +
                                            "  \"message\": \"No existe tipo de prestamo\",\n" +
                                            "  \"status\": 400\n" +
                                            "}"
                            )
                            )
                    )
            }
    )
    public Mono<ServerResponse> registrar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Solicitud.class)
                .flatMap(registrarSolicitudUseCase::registrar)
                .flatMap(solicitudGuardada -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(solicitudGuardada));
    }
}
