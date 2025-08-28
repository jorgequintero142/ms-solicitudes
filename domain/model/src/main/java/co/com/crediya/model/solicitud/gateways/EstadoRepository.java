package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Estado;
import co.com.crediya.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface EstadoRepository {
    Mono<Estado> buscarPorId(int idEstado);
}
