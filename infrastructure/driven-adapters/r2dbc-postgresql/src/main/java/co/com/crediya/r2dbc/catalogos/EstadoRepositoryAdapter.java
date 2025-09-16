package co.com.crediya.r2dbc.catalogos;

import co.com.crediya.model.solicitud.Estado;
import co.com.crediya.model.solicitud.gateways.EstadoRepository;
import co.com.crediya.r2dbc.entity.EstadoEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class EstadoRepositoryAdapter extends ReactiveAdapterOperations<
        Estado,
        EstadoEntity,
        Integer,
        EstadoReactiveRepository
        > implements EstadoRepository {


    private static final Logger logger = LoggerFactory.getLogger(EstadoRepositoryAdapter.class);


    public EstadoRepositoryAdapter(EstadoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Estado.class));
    }


    @Override
    public Mono<Estado> buscarPorId(int idEstado) {
        logger.warn("buscando  estado con id {}", idEstado);
        return super.findById(idEstado);
    }
}
