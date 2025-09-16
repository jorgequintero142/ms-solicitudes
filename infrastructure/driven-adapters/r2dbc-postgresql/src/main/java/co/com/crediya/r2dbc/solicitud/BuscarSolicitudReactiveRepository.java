package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BuscarSolicitudReactiveRepository extends ReactiveCrudRepository<DetalleSolicitudDTO, Integer>, ReactiveQueryByExampleExecutor<DetalleSolicitudDTO> {

    @Query("""
            WITH deuda_por_cliente AS (
                SELECT
                    s1.documento_identidad,
                    SUM( POWER( (1 + tp1.tasa_interes/100), s1.plazo) * s1.monto) AS total_deuda
                FROM solicitud s1
                INNER JOIN tipo_prestamo tp1
                    ON tp1.id_tipo_prestamo = s1.id_tipo_prestamo
                WHERE s1.id_estado = 2
                GROUP BY s1.documento_identidad
            )

            SELECT
                s.monto,
                s.plazo,
                s.email,
                s.documento_identidad,
                tp.nombre AS tipo_prestamo,
                e.nombre AS estado,
                tp.tasa_interes,
                COALESCE(dpc.total_deuda, 0) AS total_solicitudes_aprobadas 
            FROM solicitud s
            INNER JOIN tipo_prestamo tp
                ON tp.id_tipo_prestamo = s.id_tipo_prestamo
            INNER JOIN estado e
                ON e.id_estado = s.id_estado
            LEFT JOIN deuda_por_cliente dpc
                ON dpc.documento_identidad = s.documento_identidad
             WHERE s.id_estado IN (:estados)
                   ORDER BY s.id_solicitud ASC
                   LIMIT :limite OFFSET :offset
        """)
    Flux<DetalleSolicitudDTO> buscarSolicitudes(List<Integer> estados, int limite, int offset);
}
