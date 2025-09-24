package co.com.crediya.usecase.aprobarrechazarsolicitud;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.gateways.PublicadorSQSService;
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
    private final PublicadorSQSService publicadorSQSService;
    private final SolicitudRepository solicitudRepository;
    private final List<String> ESTADOS_PERMITIDOS = Arrays.asList("APROBADA", "RECHAZADA");
    private final ClienteWebClientes clienteWebClientes;
    private final String ROL_PERMITIDO = "Asesor";

    public Mono<String> aprobarRechazar(Integer idSolicitud, String estado) {
        return validarPermisos()
                .then(validarEstado(estado)
                        .switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ESTADO_ENVIADO_NO_PERMITDO)))
                        .flatMap(this::homologarEstado)
                        .flatMap(idEstado ->
                                solicitudRepository.aprobarRechazar(idSolicitud, idEstado)
                                        .flatMap(solicitud ->

                                                clienteWebClientes.buscarCliente(solicitud.getDocumentoIdentidad())
                                                        .flatMap(usuarioResponse -> {
                                                            ReporteAprobarRechazar reporte = ReporteAprobarRechazar
                                                                    .builder()
                                                                    .idSolicitud(idSolicitud)
                                                                    .email(solicitud.getEmail())
                                                                    .estado(estado)
                                                                    .nombre(usuarioResponse.getData().getNombre())
                                                                    .build();
                                                            return publicadorSQSService.send(reporte)
                                                                    .thenReturn(PROCESO_OK);
                                                        })
                                        )));
    }

    private Mono<String> validarEstado(String estado) {
        return Mono.defer(() -> {
                    if (ESTADOS_PERMITIDOS.contains(estado)) {
                        return Mono.just(estado);
                    }
                    return Mono.empty();
                }
        );
    }

    public Mono<Void> validarPermisos() {
        return clienteWebClientes.buscarUsuarioPorToken().flatMap(informacionUsuarioToken -> {
            if (ROL_PERMITIDO.equals(informacionUsuarioToken.getData().getRol())) {
                return Mono.empty();
            }
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_ROL_CREAR_SOLICITUD));
        });
    }

    private Mono<Integer> homologarEstado(String estado) {
        return Mono.just(EstadosSolicitudEnum.buscarCodigo(estado));
    }

    public Mono<Void> aprobarAutoSolicitud(int idSolicitud, int idEstado) {
        return solicitudRepository.aprobarRechazar(idSolicitud, idEstado)
                .onErrorMap(e -> {
                    e.printStackTrace();
                    return new RuntimeException(Constantes.ERROR_ACTUALIZANDO_ESTADO);
                })
                .then();
    }

}
