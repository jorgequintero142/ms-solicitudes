package co.com.crediya.usecase.aprobarrechazarsolicitud;

import co.com.crediya.model.aprobarrechazarsolicitud.ReportarCreditoAprobado;
import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.gateways.PublicadorSQSService;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.buscarsolicitudes.EstadosSolicitudEnum;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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

                                                            Mono<Void> enviarCreditoAprobado = Mono.empty();
                                                            /*if ("APROBADA".equalsIgnoreCase(estado)) {
                                                                ReportarCreditoAprobado creditoAprobado = ReportarCreditoAprobado.builder()
                                                                        .montoTotalPrestamos(solicitud.getMonto())
                                                                        .totalPrestamosAprobados(1)
                                                                        .build();

                                                                enviarCreditoAprobado = reportarCreditoAprobado(creditoAprobado);
                                                            }*/


                                                            return Mono.when(
                                                                    publicadorSQSService.send(reporte),
                                                                    enviarCreditoAprobado
                                                            ).thenReturn(PROCESO_OK);
                                                        })
                                        )));
    }


    public Mono<Void> reportarCreditoAprobado(ReportarCreditoAprobado reportarCreditoAprobado) {
        System.out.println("reportarCreditoAprobado->"+reportarCreditoAprobado);
        return publicadorSQSService.send(reportarCreditoAprobado)
                .then();
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
        System.out.println("aprobarAutoSolicitud->"+idSolicitud+"<>"+idEstado);

        return solicitudRepository.aprobarRechazar(idSolicitud, idEstado)
                /*.flatMap(solicitud -> {
                    System.out.println("Ok actualizacion.. validando estado");
                    if (idEstado == 2) {
                        System.out.println("Solicitud publicar mensaje aprobado ");
                        ReportarCreditoAprobado creditoAprobado = ReportarCreditoAprobado
                                .builder()
                                .montoTotalPrestamos(BigDecimal.TEN)
                                .build();
                        return reportarCreditoAprobado(creditoAprobado);
                    } else {
                        System.out.println("Ok actualizacion.. no se hizo nada ");
                    }
                    return Mono.empty();
                })*/
                .onErrorMap(e -> {
                    e.printStackTrace();
                    return new RuntimeException(Constantes.ERROR_ACTUALIZANDO_ESTADO);
                })
                .then();
    }

}
