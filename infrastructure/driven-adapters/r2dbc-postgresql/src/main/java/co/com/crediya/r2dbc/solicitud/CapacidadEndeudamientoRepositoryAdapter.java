package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.capacidadendeudamiento.TotalDeudaMensual;
import co.com.crediya.model.capacidadendeudamiento.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CapacidadEndeudamientoRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        TotalDeudaMensual,
        Integer,
        CapacidadEndeudamientoReactiveRepository
        > implements CapacidadEndeudamientoRepository {

    protected CapacidadEndeudamientoRepositoryAdapter(CapacidadEndeudamientoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }


    @Override
    public Mono<TotalDeudaMensual> calcularDeudaMensual(String numeroDocumento) {
        return repository.calcularDeudaMensual(numeroDocumento);
    }
}
