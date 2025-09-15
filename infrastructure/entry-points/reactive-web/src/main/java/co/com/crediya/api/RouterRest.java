package co.com.crediya.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({@RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.POST,
            beanClass = SolicitudHandler.class,
            beanMethod = "registrar"
    ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "buscar"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {

        return RouterFunctions.route()
                .POST("/api/v1/solicitud", handler::registrar)
                .GET("/api/v1/solicitud", handler::buscar)
                .build();
    }
}
