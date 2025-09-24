package co.com.crediya.model.gateways;

import reactor.core.publisher.Mono;

public interface PublicadorSQSService {
    Mono<String> send(PublicadoraMensajesSQS sender);
}