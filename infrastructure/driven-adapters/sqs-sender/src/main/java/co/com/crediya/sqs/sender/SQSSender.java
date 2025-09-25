package co.com.crediya.sqs.sender;

import co.com.crediya.model.gateways.PublicadorSQSService;
import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements PublicadorSQSService {
    private final GeneradorMensajes generadorMensajes;


    private final SqsAsyncClient client;


    @Override
    public Mono<String> send(PublicadoraMensajesSQS publicador) {
        return Mono.fromCallable(() -> generadorMensajes.buildRequest(publicador))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    log.error("buildRequest.error--> {}", throwable.getMessage());
                })
                .flatMap(request -> {
                    log.debug("request.enviado--> {}", request.messageBody());
                    return Mono.fromFuture(client.sendMessage(request));
                })
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    log.error("client.sendMessage.error--> {}", throwable.getMessage());
                })
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

}
