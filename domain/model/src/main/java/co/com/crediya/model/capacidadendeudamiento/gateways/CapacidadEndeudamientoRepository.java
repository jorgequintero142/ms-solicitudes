package co.com.crediya.model.capacidadendeudamiento.gateways;

import co.com.crediya.model.capacidadendeudamiento.TotalDeudaMensual;
import reactor.core.publisher.Mono;

public interface CapacidadEndeudamientoRepository {
    Mono<TotalDeudaMensual> calcularDeudaMensual(String numeroDocumento);
}
