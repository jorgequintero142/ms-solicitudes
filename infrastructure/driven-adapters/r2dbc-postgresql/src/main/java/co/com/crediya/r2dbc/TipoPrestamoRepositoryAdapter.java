package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.r2dbc.entity.SolicitudEntity;
import co.com.crediya.r2dbc.entity.TipoPrestamoEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo,
        TipoPrestamoEntity,
        Integer,
        TipoPrestamoReactiveRepository
        > implements TipoPrestamoRepository  {

    private static final Logger logger = LoggerFactory.getLogger(TipoPrestamoRepositoryAdapter.class);

    public TipoPrestamoRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class));
    }


    @Override
    public Mono<TipoPrestamo> buscarPorId(int idTipoPrestamo) {
        logger.debug("Buscando prestamo por idTipoPrestamo = "+idTipoPrestamo);
        return repository.findById(idTipoPrestamo).map(found-> mapper.map(found,TipoPrestamo.class)).switchIfEmpty(Mono.empty());

    }
}
