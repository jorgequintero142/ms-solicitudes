package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.dto.InformacionUsuarioToken;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import reactor.core.publisher.Mono;

public interface ClienteWebClientes {
    Mono<UsuarioResponse> buscarCliente(String numeroDocumento);

    Mono<InformacionUsuarioToken> buscarUsuarioPorToken(String token);
}
