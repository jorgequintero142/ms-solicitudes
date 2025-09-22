package co.com.crediya.model.capacidadendeudamiento.gateways;

import co.com.crediya.model.capacidadendeudamiento.CapacidadEndeudamiento;
import reactor.core.publisher.Mono;

public interface CapacidadEndeudamientoService {
    Mono<Void> calcularCapacidadEndeudamiento(CapacidadEndeudamiento capacidadEndeudamiento);
}
