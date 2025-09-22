package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.capacidadendeudamiento.TotalDeudaMensual;
import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CapacidadEndeudamientoReactiveRepository extends ReactiveCrudRepository<TotalDeudaMensual, Integer>, ReactiveQueryByExampleExecutor<TotalDeudaMensual> {

    @Query("""
                SELECT
                 COALESCE(
                    SUM(
                        ROUND(
                           (
                                s.monto * ((tp.tasa_interes/12.0) * POWER(1 + (tp.tasa_interes/12.0), s.plazo))
                                / (POWER(1 + (tp.tasa_interes/12.0), s.plazo) - 1)
                            )
                        , 2)
                    ),0 ) AS total_deuda_mensual
                FROM solicitud s
                INNER JOIN tipo_prestamo tp
                    ON tp.id_tipo_prestamo = s.id_tipo_prestamo
                WHERE s.documento_identidad = :numeroDocumento
                  AND s.id_estado = 2
            """)
    Mono<TotalDeudaMensual> calcularDeudaMensual(String numeroDocumento);
}
