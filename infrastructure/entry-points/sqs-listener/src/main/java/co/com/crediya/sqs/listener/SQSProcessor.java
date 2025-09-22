package co.com.crediya.sqs.listener;

import co.com.crediya.usecase.aprobarrechazarsolicitud.AprobarRechazarSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final AprobarRechazarSolicitudUseCase aprobarRechazarSolicitudUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        String[] parametros = message.body().split(",");
        int idSolicitud = Integer.parseInt(parametros[0]);
        int idCodigo = Integer.parseInt(parametros[1]);
        return aprobarRechazarSolicitudUseCase.aprobarAutoSolicitud(idSolicitud, idCodigo)
                .doOnSuccess(unused ->
                        log.info("Se actualiza la solicitud con id {} al estado {}", idSolicitud, idCodigo))
                .doOnError(e ->
                        log.error("Error actualizando solicitud {} al estado {}: {}", idSolicitud, idCodigo, e.getMessage()));
    }
}
