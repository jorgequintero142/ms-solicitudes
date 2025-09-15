package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.detallesolicitud.ParametrosBusqueda;
import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import co.com.crediya.model.detallesolicitud.gateways.DetalleSolicitudRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class BuscarSolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        DetalleSolicitudDTO,
        Integer,
        BuscarSolicitudReactiveRepository
        > implements DetalleSolicitudRepository {

    protected BuscarSolicitudRepositoryAdapter(BuscarSolicitudReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    @Override
    public Flux<DetalleSolicitudDTO> buscarSolicitudes(ParametrosBusqueda parametrosBusqueda) {
               return repository.buscarSolicitudes(parametrosBusqueda.getCodigosEstado(), parametrosBusqueda.totalRegistros,parametrosBusqueda.pagina);
    }
}
