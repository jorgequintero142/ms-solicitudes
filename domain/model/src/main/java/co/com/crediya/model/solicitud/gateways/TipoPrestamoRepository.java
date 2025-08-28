package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> buscarPorId(int idTipoPrestamo);
}
