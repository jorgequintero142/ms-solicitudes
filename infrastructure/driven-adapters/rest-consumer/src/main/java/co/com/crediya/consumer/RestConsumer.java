package co.com.crediya.consumer;

import co.com.crediya.model.solicitud.dto.InformacionUsuarioToken;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ClienteWebClientes {
    private final WebClient client;

    @Override
    public Mono<UsuarioResponse> buscarCliente(String documentoIdentidad) {

        return leerTokenFromSecurityContext()
                .onErrorResume(throwable ->  Mono.error(new ParametroNoValidoException("Fockiu")))
                .switchIfEmpty(Mono.error(new ParametroNoValidoException("Fockiu 2")))
                .flatMap(tokens-> client
                .get()
                .uri("/api/v1/usuarios/{documentoIdentidad}", documentoIdentidad)
                .header("Authorization", "Bearer " + tokens)
                .retrieve()
                .bodyToMono(UsuarioResponse.class));
    }

    @Override
    public Mono<InformacionUsuarioToken> buscarUsuarioPorToken() {
             return leerTokenFromSecurityContext()
                     .onErrorResume(throwable ->  Mono.error(new ParametroNoValidoException("Fockiu")))
                     .switchIfEmpty(Mono.error(new ParametroNoValidoException("Fockiu 2")))
                     .flatMap(token ->  client
                             .get()
                             .uri("/api/v1/token")
                             .header("Authorization", "Bearer " + token)
                             .retrieve()
                             .bodyToMono(InformacionUsuarioToken.class));
    }
    public Mono<String> leerTokenFromSecurityContext() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getCredentials().toString());
    }

}
