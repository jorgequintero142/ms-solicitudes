package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.r2dbc.entity.SolicitudEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class SolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Integer,
        SolicitudReactiveRepository
        > implements SolicitudRepository {

    private static final int ESTADO_PENDIENTE = 1;
    private static final Logger logger = LoggerFactory.getLogger(SolicitudRepositoryAdapter.class);
    private final TransactionalOperator operadorTransaccion;

    public SolicitudRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator operadorTransaccion) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
        this.operadorTransaccion = operadorTransaccion;
    }

    @Override
    public Mono<Solicitud> registrar(Solicitud solicitud) {
        SolicitudEntity entity = mapper.map(solicitud, SolicitudEntity.class);
        entity.setIdSolicitud(null);
        entity.setIdEstado(ESTADO_PENDIENTE);
        return repository.save(entity)
                .map(saved ->
                        mapper.map(saved, Solicitud.class)
                ).as(operadorTransaccion::transactional)
                .doOnNext(u -> logger.debug("Se ha registrado una nueva solicitud {}", solicitud));
    }
}
