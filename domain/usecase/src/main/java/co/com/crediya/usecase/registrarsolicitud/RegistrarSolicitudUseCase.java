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
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {


    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private final ClienteWebClientes clienteWebClientes;


    public Mono<SolicitudCreada> registrar(Solicitud solicitud) {

        return validarSolicitud(solicitud)
                .flatMap(solicitudRepository::registrar)
                .flatMap(solicitudCreada ->
                        buscarTipoPrestamo(solicitud)
                                .map(tipoPrestamo -> {
                                    solicitudCreada.setTipoPrestamo(tipoPrestamo.getNombre());
                                    return solicitudCreada;
                                })
                ).flatMap(solicitudCreada ->
                        buscarEstado(solicitudCreada.getIdEstado())
                                .map(estado -> {
                                    solicitudCreada.setEstado(estado.getNombre());
                                    return solicitudCreada;
                                })
                );
    }

    private Mono<Estado> buscarEstado(int idEstado) {
        return estadoRepository.buscarPorId(idEstado);
    }

    private Mono<Solicitud> validarSolicitud(Solicitud solicitud) {
        return validarDatosBasicos(solicitud)
                .then(validarTipoPrestamo(solicitud))
                .then(validarCliente(solicitud));
    }

    private Mono<Void> validarDatosBasicos(Solicitud solicitud) {
        if (solicitud.getPlazo() < Constantes.PLAZO_MINIMO) {
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_PLAZO));
        }
        if (solicitud.getMonto() == null || solicitud.getMonto().compareTo(Constantes.MONTO_MINIMO) <= 0) {
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_VALOR_MONTO));
        }
        return Mono.empty();
    }

    private Mono<TipoPrestamo> buscarTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.buscarPorId(solicitud.getIdTipoPrestamo());
    }


    private Mono<TipoPrestamo> validarTipoPrestamo(Solicitud solicitud) {
        return buscarTipoPrestamo(solicitud).switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ERROR_TIPO_PRESTAMO)));
    }

    private Mono<Solicitud> validarCliente(Solicitud solicitud) {
        return clienteWebClientes.buscarCliente(solicitud.getDocumentoIdentidad())
                .switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)))
                .flatMap(clienteRespuesta -> {
                    solicitud.setEmail(clienteRespuesta.getData().getEmail());
                    return Mono.just(solicitud);
                })
                .onErrorResume(e -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)));
    }
}