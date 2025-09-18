package co.com.crediya.sqs.sender;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.aprobarrechazarsolicitud.gateways.AprobarRechazarSolicitudService;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements AprobarRechazarSolicitudService {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public Mono<String> send(String message) {
         return Mono.fromCallable(() -> buildRequest(message))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    log.error("buildRequest.error--> {}",throwable.getMessage());
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                 .doOnError(throwable -> {
                     throwable.printStackTrace();
                     log.error("client.sendMessage.error--> {}",throwable.getMessage());
                 })
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    public Mono<String> aprobarRechazarSolicitud(ReporteAprobarRechazar reporte) {
        String mensaje = String.format(
                "%s,%d,%s,%s",
                reporte.getNombre(),
                reporte.getIdSolicitud(),
                reporte.getEstado(),
                reporte.getEmail()
        );

        return this.send(mensaje)
                .doOnSuccess(id -> log.info("Mensaje enviado a SQS con ID {}", id))
                .onErrorMap(e -> {
                    log.error("Error enviando mensaje a SQS", e);
                    return new ParametroNoValidoException("Error al enviar mensaje a SQS: " + e.getMessage());
                });
    }
}
