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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({@RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.POST,
            beanClass = SolicitudHandler.class,
            beanMethod = "registrar"
    )})
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
        return route(
                POST("/api/v1/solicitud"), handler::registrar);
    }
}
