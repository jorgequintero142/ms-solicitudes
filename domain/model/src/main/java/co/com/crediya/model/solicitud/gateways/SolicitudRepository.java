package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {

    Mono<SolicitudCreada> registrar(Solicitud solicitud);
}
