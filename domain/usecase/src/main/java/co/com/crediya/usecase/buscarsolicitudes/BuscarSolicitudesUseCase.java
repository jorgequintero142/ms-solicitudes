package co.com.crediya.usecase.buscarsolicitudes;

import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import co.com.crediya.model.detallesolicitud.ParametrosBusqueda;
import co.com.crediya.model.detallesolicitud.gateways.DetalleSolicitudRepository;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BuscarSolicitudesUseCase {

    private final DetalleSolicitudRepository solicitudRepository;
    private final ClienteWebClientes clienteWebClientes;

    public Flux<DetalleSolicitudDTO> buscarSolicitudes(ParametrosBusqueda parametrosBusqueda) {

       return  Flux.fromArray(parametrosBusqueda.estados.split(","))
                .map(String::trim)
                .map(EstadosSolicitudEnum::buscarCodigo)
                .collectList()
                .doOnNext(parametrosBusqueda::setCodigosEstado)
                .flatMapMany(list -> solicitudRepository.buscarSolicitudes(parametrosBusqueda))
               .flatMap(detalleSolicitudDTO -> obtenerInformacionUsuario(detalleSolicitudDTO));

         }

    private Mono<DetalleSolicitudDTO> obtenerInformacionUsuario(DetalleSolicitudDTO detalleSolicitudDTO) {
        return buscarInformacionCliente(detalleSolicitudDTO.getDocumentoIdentidad())
                .map(datosUsuario -> {
                    detalleSolicitudDTO.setNombre(String.format("%s %s",datosUsuario.getNombre(),datosUsuario.getApellido()));
                    detalleSolicitudDTO.setSalarioBase(datosUsuario.getSalarioBase());
                    return detalleSolicitudDTO;
                });
    }

    private Mono<DatosUsuario> buscarInformacionCliente(String documentoIdentidad) {
        return clienteWebClientes.buscarCliente(documentoIdentidad)
                .onErrorResume(e ->  Mono.error(new ParametroNoValidoException(Constantes.ERROR_DE_COMUNICACION)))
                .switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)))
                .flatMap(cliente ->
                     Mono.just(cliente.getData())
                );
    }
}
