package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudUnica;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {

    Mono<SolicitudUnica> registrar(Solicitud solicitud);

    Mono<Solicitud> aprobarRechazar(Integer idSolicitud, Integer idEstado);
}
