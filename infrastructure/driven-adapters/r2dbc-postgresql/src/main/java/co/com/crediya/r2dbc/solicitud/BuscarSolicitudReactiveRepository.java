package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BuscarSolicitudReactiveRepository extends ReactiveCrudRepository<DetalleSolicitudDTO, Integer>, ReactiveQueryByExampleExecutor<DetalleSolicitudDTO> {

    @Query("""
        SELECT s.monto,
                          s.plazo,
                          s.email,
                          s.documento_identidad,
                          tp.nombre AS tipo_prestamo,
                          e.nombre AS estado,
                          tp.tasa_interes
                   FROM solicitud s
                   INNER JOIN tipo_prestamo tp ON tp.id_tipo_prestamo = s.id_tipo_prestamo
                   INNER JOIN estado e ON e.id_estado = s.id_estado
                   WHERE s.id_estado IN (:estados)
                   ORDER BY s.id_solicitud ASC
                   LIMIT :limite OFFSET :offset
        """)
    Flux<DetalleSolicitudDTO> buscarSolicitudes(List<Integer> estados, int limite, int offset);
}
