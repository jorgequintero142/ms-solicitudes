package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Estado;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.calcularcapacidadendeudamiento.CalcularCapacidadEndeudamientoUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private final ClienteWebClientes clienteWebClientes;
    private static final String ROL_CREAR_SOLICITUD = "Cliente";
    private final CalcularCapacidadEndeudamientoUseCase calcularCapacidadEndeudamientoUseCase;

    public Mono<SolicitudCreada> registrar(Solicitud solicitud) {
        return validarSolicitud(solicitud)
                .flatMap(solicitudRepository::registrar)
                .flatMap(nuevaSolicitud -> buscarEstado(Constantes.ESTADO_PENDIENTE)
                        .flatMap(estado -> buscarTipoPrestamo(nuevaSolicitud.getIdTipoPrestamo())
                                .flatMap(tipoPrestamo -> {
                                    SolicitudCreada solicitudCreada = SolicitudCreada.builder()
                                            .email(nuevaSolicitud.getEmail())
                                            .monto(nuevaSolicitud.getMonto())
                                            .plazo(nuevaSolicitud.getPlazo())
                                            .estado(estado.getNombre())
                                            .documentoIdentidad(nuevaSolicitud.getDocumentoIdentidad())
                                            .tipoPrestamo(tipoPrestamo.getNombre())
                                            .build();

                                    if (solicitud.isValidacionAutomatica()) {
                                        System.out.println("SI validacion automatica.....");
                                        return realizarValidacionAutomatica(solicitud, tipoPrestamo.getTasaInteres(),nuevaSolicitud.getIdSolicitud())
                                                .thenReturn(solicitudCreada);
                                    }

                                    return Mono.just(solicitudCreada);
                                })
                        )
                );
    }

    private Mono<Estado> buscarEstado(int idEstado) {
        return estadoRepository.buscarPorId(idEstado);
    }

    private Mono<Solicitud> validarSolicitud(Solicitud solicitud) {
        return validarCliente(solicitud)
                .then(validarPlazo(solicitud))
                .then(validarTipoPrestamo(solicitud))
                .then(validarMonto(solicitud))
                .then(Mono.just(solicitud));
    }


    private Mono<Void> validarPlazo(Solicitud solicitud) {
        return Mono.defer(() -> {
            if (solicitud.getPlazo() < Constantes.PLAZO_MINIMO)  {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_PLAZO));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> validarMonto(Solicitud solicitud) {

        return Mono.defer(() -> {
            if (solicitud.getMonto() == null || solicitud.getMonto().compareTo(Constantes.MONTO_MINIMO) <= 0)  {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_VALOR_MONTO));
            }
            return Mono.empty();
        });
    }

    private Mono<TipoPrestamo> buscarTipoPrestamo(int solicitud) {
        return tipoPrestamoRepository.buscarPorId(solicitud);
    }


    private Mono<TipoPrestamo> validarTipoPrestamo(Solicitud solicitud) {

        return buscarTipoPrestamo(solicitud.getIdTipoPrestamo()).switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ERROR_TIPO_PRESTAMO)));
    }

    private Mono<Solicitud> validarCliente(Solicitud solicitud) {

        return clienteWebClientes
                .buscarUsuarioPorToken()
                .onErrorResume(throwable -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)))
                .flatMap(informacionUsuarioToken ->
                        verificarPermisos(informacionUsuarioToken.getData().getDocumento(), informacionUsuarioToken.getData().getRol(), solicitud)
                                .flatMap(solicitud1 -> {
                                    solicitud.setEmail(informacionUsuarioToken.getData().getSubject());
                                    return Mono.just(solicitud);
                                })
                );
    }

    Mono<Solicitud> verificarPermisos(String documento, String rol, Solicitud solicitud) {
        return Mono.defer(() -> {
            if (!documento.equals(solicitud.getDocumentoIdentidad())) {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_DOCUMENTO_SOLICITUD));
            }
            if (!ROL_CREAR_SOLICITUD.equals(rol)) {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_ROL_CREAR_SOLICITUD));
            }
            return Mono.just(solicitud);
        });
    }

    Mono<Void> realizarValidacionAutomatica(Solicitud solicitud, BigDecimal tasaInteres, int idSolicitud) {
        System.out.println("Continua validacion automatica.....1");
        return clienteWebClientes.buscarCliente(solicitud.getDocumentoIdentidad()).flatMap(
                usuarioResponse -> {
                    System.out.println("Continua validacion automatica.....2");
                   return  calcularCapacidadEndeudamientoUseCase
                           .calcularCapacidadEndeudamiento(solicitud,usuarioResponse.getData(), tasaInteres, idSolicitud)
                           .flatMap(s -> Mono.empty());
                }
        );
    }
}