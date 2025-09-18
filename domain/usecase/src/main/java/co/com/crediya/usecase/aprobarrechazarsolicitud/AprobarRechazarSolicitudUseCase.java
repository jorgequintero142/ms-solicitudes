package co.com.crediya.usecase.aprobarrechazarsolicitud;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.aprobarrechazarsolicitud.gateways.AprobarRechazarSolicitudService;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.buscarsolicitudes.EstadosSolicitudEnum;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class AprobarRechazarSolicitudUseCase {
    private static final String PROCESO_OK = "Se ha procesado la solicitud de manera exitosa";
    private final AprobarRechazarSolicitudService aprobarRechazarSolicitudService;
    private final SolicitudRepository solicitudRepository;
    private final List<String> ESTADOS_PERMITIDOS = Arrays.asList("APROBADA","RECHAZADA");
    private final ClienteWebClientes clienteWebClientes;

    public Mono<String> aprobarRechazar(Integer idSoliciud, String estado) {
            return  validarEstado(estado)
                    .switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ESTADO_ENVIADO_NO_PERMITDO)))
                    .flatMap(this::homologarEstado)
                    .flatMap(idEstado ->
                            solicitudRepository.aprobarRechazar(idSoliciud,idEstado)
                                    .flatMap(solicitud ->
                                    clienteWebClientes.buscarCliente(solicitud.getDocumentoIdentidad())
                                            .flatMap(usuarioResponse -> {
                                               ReporteAprobarRechazar reporte =  ReporteAprobarRechazar
                                                        .builder()
                                                        .idSolicitud(idSoliciud)
                                                        .email(solicitud.getEmail())
                                                        .estado(estado)
                                                        .nombre(usuarioResponse.getData().getNombre())
                                                        .build();
                                                return aprobarRechazarSolicitudService.aprobarRechazarSolicitud(reporte)
                                                        .thenReturn(PROCESO_OK);
                                            })
                                    ));
    }

    private Mono<String> validarEstado(String estado) {
        return Mono.defer(()-> {
                    if (ESTADOS_PERMITIDOS.contains(estado)) {
                        return Mono.just(estado);
                    }
                    return Mono.empty();
            }
        );
    }

    private Mono<Integer> homologarEstado(String estado) {
      return Mono.just(EstadosSolicitudEnum.buscarCodigo(estado));
    }

}
