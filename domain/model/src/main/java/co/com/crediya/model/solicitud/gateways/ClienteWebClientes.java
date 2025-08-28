package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.request.UsuarioResponse;
import reactor.core.publisher.Mono;

public interface ClienteWebClientes {
   Mono<UsuarioResponse> buscarCliente(String numeroDocumento);
}
