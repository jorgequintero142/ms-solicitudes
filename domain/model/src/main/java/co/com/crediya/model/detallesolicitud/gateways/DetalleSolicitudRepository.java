package co.com.crediya.model.detallesolicitud.gateways;

import co.com.crediya.model.detallesolicitud.ParametrosBusqueda;
import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import reactor.core.publisher.Flux;

public interface DetalleSolicitudRepository {
    Flux<DetalleSolicitudDTO> buscarSolicitudes(ParametrosBusqueda parametrosBusqueda);
}
