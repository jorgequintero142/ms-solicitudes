package co.com.crediya.model.aprobarrechazarsolicitud.gateways;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import reactor.core.publisher.Mono;

public interface AprobarRechazarSolicitudService {
    Mono<String> aprobarRechazarSolicitud(ReporteAprobarRechazar reporteAprobarRechazar);
}
