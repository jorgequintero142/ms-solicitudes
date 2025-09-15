package co.com.crediya.consumer;

import co.com.crediya.model.solicitud.dto.InformacionUsuarioToken;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ClienteWebClientes {
    private final WebClient client;

    @Override
    public Mono<UsuarioResponse> buscarCliente(String documentoIdentidad, String token) {
        return client
                .get()
                .uri("/api/v1/usuarios/{documentoIdentidad}", documentoIdentidad)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UsuarioResponse.class);
    }

    @Override
    public Mono<InformacionUsuarioToken> buscarUsuarioPorToken(String token) {
             return client
                .get()
                .uri("/api/v1/token")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(InformacionUsuarioToken.class);
    }
}
