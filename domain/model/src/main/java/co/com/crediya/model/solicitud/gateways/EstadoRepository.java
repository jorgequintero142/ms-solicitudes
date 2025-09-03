package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Estado;
import reactor.core.publisher.Mono;

public interface EstadoRepository {
    Mono<Estado> buscarPorId(int idEstado);
}
