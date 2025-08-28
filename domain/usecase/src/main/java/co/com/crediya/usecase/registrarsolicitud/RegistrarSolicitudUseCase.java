package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private final int PLAZO_MINIMO = 6;
    private final BigDecimal MONTO_MINIMO = new BigDecimal("0");
    private final int ESTADO_PENDIENTE = 1;
    private final SolicitudRepository usuarioRepositorio;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final ClienteWebClientes clienteWebClientes;
   // private static final Logger logger = LoggerFactory.getLogger(RegistrarSolicitudUseCase.class);
    public Mono<Solicitud> registrar(Solicitud solicitud) {

        return validarSolicitud(solicitud).map(s-> {s.setIdEstado(ESTADO_PENDIENTE); return s;}).flatMap(usuarioRepositorio::registrar);
    }

    private Mono<Solicitud> validarSolicitud(Solicitud solicitud) {
        return validarDatosBasicos(solicitud)
                .then(validarTipoPrestamo(solicitud))
                .then(validarCliente(solicitud));
    }

    private Mono<Void> validarDatosBasicos(Solicitud solicitud) {
        if ( solicitud.getPlazo() < PLAZO_MINIMO) {
            return Mono.error(new ParametroNoValidoException("Valor del monto no es correcto"));
        }
        if (solicitud.getMonto() == null || solicitud.getMonto().compareTo(MONTO_MINIMO) <= 0) {
            return Mono.error(new ParametroNoValidoException("Valor del monto no es correcto"));
        }
        return Mono.empty();
    }

    private Mono<Void> validarTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.buscarPorId(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new ParametroNoValidoException("No existe tipo de prestamo")))
                .then();
    }

    private Mono<Solicitud> validarCliente(Solicitud solicitud) {
        return clienteWebClientes.buscarCliente(solicitud.getDocumentoIdentidad())
                .flatMap(cliente -> {
                    if (cliente == null) {
                        return Mono.error(new ParametroNoValidoException("No se encontró información del cliente"));
                    }
                    solicitud.setEmail(cliente.getEmail());
                    return Mono.just(solicitud);
                })
                .onErrorResume(e -> {
                    return Mono.error(new ParametroNoValidoException("No se encontró información del cliente"));
                });
    }


}