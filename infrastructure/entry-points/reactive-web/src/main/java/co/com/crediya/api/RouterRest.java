package co.com.crediya.api;

import co.com.crediya.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({@RouterOperation(
            path = "/api/v1/solicitud",
            beanClass = SolicitudHandler.class,
            beanMethod = "registrar",
            operation = @Operation(
                    summary = "Crear un nueva solicitud",
                    description = "Crea una nueva solicitud en el sistema",
                    tags = {"Solicitudes"},
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Solicitud.class
                                    )
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Solicitud creada",
                                    content = @Content(
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = Solicitud.class
                                            )
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Validación fallida"
                            )
                    }
            )
    )})
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
        return route(
                POST("/api/v1/solicitud"), handler::registrar);
    }
}
