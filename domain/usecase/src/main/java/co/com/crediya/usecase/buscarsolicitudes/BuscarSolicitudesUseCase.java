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

    public Flux<DetalleSolicitudDTO> buscarSolicitudes(ParametrosBusqueda parametrosBusqueda, String token) {

       return  Flux.fromArray(parametrosBusqueda.estados.split(","))
                .map(String::trim)
                .map(EstadosSolicitudEnum::buscarCodigo)
                .collectList()
                .doOnNext(parametrosBusqueda::setCodigosEstado)
                .flatMapMany(list -> solicitudRepository.buscarSolicitudes(parametrosBusqueda))
               .flatMap(detalleSolicitudDTO -> obtenerInformacionUsuario(detalleSolicitudDTO,token));

         }

    private Mono<DetalleSolicitudDTO> obtenerInformacionUsuario(DetalleSolicitudDTO detalleSolicitudDTO, String token) {
        return buscarInformacionCliente(detalleSolicitudDTO.getDocumentoIdentidad(), token)
                .map(datosUsuario -> {

                    detalleSolicitudDTO.setNombre(String.format("%s %s",datosUsuario.getNombre(),datosUsuario.getApellido()));
                    detalleSolicitudDTO.setSalarioBase(datosUsuario.getSalarioBase());
                    return detalleSolicitudDTO;
                });
    }

    private Mono<DatosUsuario> buscarInformacionCliente(String documentoIdentidad, String token) {
        return clienteWebClientes.buscarCliente(documentoIdentidad, token)
                .onErrorResume(e ->  Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)))
                .flatMap(cliente ->
                     Mono.just(cliente.getData())
                );
    }
}
