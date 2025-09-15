package co.com.crediya.api;

import co.com.crediya.api.dto.RespuestaApi;
import co.com.crediya.model.detallesolicitud.ParametrosBusqueda;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.buscarsolicitudes.BuscarSolicitudesUseCase;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final BuscarSolicitudesUseCase buscarSolicitudesUseCase;

    private static final String SOLICITUD_CREADA = "Solicitud creada";
    private static final String RESULTADOS_BUSQUEDA = "Resultados de la búsqueda";
    private static final int CODIGO_ESTADO_OK = 200;

    private static final Logger logger = LoggerFactory.getLogger(SolicitudHandler.class);

    @Operation(
            summary = "Crear un nueva solicitud de prestamo",
            description = "Crea una nueva solicitud en el sistema",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"Solicitudes"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """ 
                                            {
                                              "monto": 100000,
                                              "plazo": 6,
                                              "documentoIdentidad": "1757553740",
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
                                                        "estado": 200,
                                                        "mensaje": "Solicitud creada",
                                                        "data": {
                                                            "estado": "Pendiente de revisión",
                                                            "tipoPrestamo": "Vivienda",
                                                            "monto": 100000,
                                                            "plazo": 6,
                                                            "documentoIdentidad": "1757553740",
                                                            "email": "Eloisa_Boyle@yahoo.com"
                                                        }
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
                                                         "estado": 400,
                                                         "mensaje": "Validación fallida",
                                                         "error": "Plazo no es correcto"
                                                     }
                                                    """
                                    )
                            )
                    )
            }
    )
    public Mono<ServerResponse> registrar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Solicitud.class)
                .doOnNext(solicitudRecibida -> logger.info("SolicitudHandler::registrar request {}", solicitudRecibida))
                .flatMap(solicitud ->
                        leerBearerToken(serverRequest)
                                .flatMap(token ->
                                    registrarSolicitudUseCase.registrar(solicitud, token)
                                )
                                .doOnSuccess(solicitudCreada -> logger.info("SolicitudHandler::registrar response {}", solicitudCreada))
                                .doOnError(error -> logger.error("SolicitudHandler::registrar error", error))

                )
                .flatMap(solicitudGuardada -> {
                            RespuestaApi<SolicitudCreada> respuesta = new RespuestaApi<>(SolicitudHandler.CODIGO_ESTADO_OK, SolicitudHandler.SOLICITUD_CREADA, solicitudGuardada);
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                        }
                );
    }

    private Mono<String> leerBearerToken(ServerRequest request) {
        return
                Mono.defer(() -> {
                    if (request.headers() == null || 
                            request.headers().firstHeader("Authorization") == null
                    ) {
                        return Mono.error(new ParametroNoValidoException(Constantes.ERROR_TOKEN));
                    }
                   return Mono.just(request.headers().firstHeader("Authorization"))
                            .filter(authHeader -> authHeader.startsWith("Bearer "))
                            .map(authHeader -> authHeader.substring("Bearer ".length()));
                });
    }



    @Operation(
            summary = "Buscador de solicitudes",
            description = "Busca solicitudes usando query params",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"Solicitudes"},
            parameters = {
                    @Parameter(
                            name = "pagina",
                            description = "Número de página",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", example = "1")
                    ),
                    @Parameter(
                            name = "cantidad",
                            description = "Cantidad de resultados",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", example = "10")
                    ),
                    @Parameter(
                            name = "estado",
                            description = "Estados de la solicitud separados por coma",
                            required = false,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "String", example = "PENDIENTE")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultados de la búsqueda",
                            content = @Content(
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "estado": 200,
                                                      "mensaje": "Resultados de la búsqueda",
                                                      "data": [
                                                        {
                                                          "monto": 100000,
                                                          "plazo": 6,
                                                          "documentoIdentidad": "9000000",
                                                          "idTipoPrestamo": 0,
                                                          "email": "clientejueves@hotmail.com"
                                                        }
                                                      ]
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
                                                         "estado": 400,
                                                         "mensaje": "Validación fallida",
                                                         "error": "Plazo no es correcto"
                                                     }
                                                    """
                                    )
                            )
                    )
            }
    )
    public Mono<ServerResponse> buscar(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String pagina = serverRequest.queryParam("pagina").orElse("1");
            String cantidad = serverRequest.queryParam("cantidad").orElse("10");
            String estados = serverRequest.queryParam("estado").orElse("PENDIENTE");
             ParametrosBusqueda parametrosBusqueda = ParametrosBusqueda
                    .builder()
                    .estados(estados)
                    .pagina(Integer.parseInt(pagina)-1)
                    .totalRegistros(Integer.parseInt(cantidad))
                    .build();
            return   leerBearerToken(serverRequest).flatMap(token ->   buscarSolicitudesUseCase.buscarSolicitudes(parametrosBusqueda, token)
                    .collectList()
                    .map(detallesSolicitud ->
                            new RespuestaApi<>(
                                    SolicitudHandler.CODIGO_ESTADO_OK,
                                    SolicitudHandler.RESULTADOS_BUSQUEDA,
                                    detallesSolicitud
                            )
                    )

                    .flatMap(respuesta ->
                            ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(respuesta)
                    ));
        });
    }

}
