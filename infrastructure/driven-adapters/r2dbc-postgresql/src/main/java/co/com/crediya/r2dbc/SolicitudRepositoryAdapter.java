package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.r2dbc.entity.SolicitudEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class SolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
    Integer,
        SolicitudReactiveRepository
> implements SolicitudRepository  {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudRepositoryAdapter.class);

    public SolicitudRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper) {
           super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    @Override
    public Mono<Solicitud> registrar(Solicitud solicitud) {
        SolicitudEntity entity = mapper.map(solicitud, SolicitudEntity.class);
        entity.setIdSolicitud(null);
            return repository.save(entity)
                .map(saved -> {
                    return mapper.map(saved, Solicitud.class);
                })
                .doOnNext(u -> logger.debug( "Se ha registrado una nueva solicitud "));
    }
}
