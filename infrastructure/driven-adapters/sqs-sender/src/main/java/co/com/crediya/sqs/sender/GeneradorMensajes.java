package co.com.crediya.sqs.sender;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.capacidadendeudamiento.CapacidadEndeudamiento;
import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import co.com.crediya.sqs.sender.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeneradorMensajes {
    private final MensajesSQSContexto mensajesSQSContexto;
    private final SQSSenderProperties properties;

    public SendMessageRequest buildRequest(PublicadoraMensajesSQS publicadoraMensajesSQS) {
        MensajesSQSStrategy estrategia = mensajesSQSContexto
                .obtenerEstrategia(definirEstrategia(publicadoraMensajesSQS));
        MensajePublicar mensajePublicar = estrategia.crearMensajePublicar(publicadoraMensajesSQS);
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageAttributes(generarAtributos(mensajePublicar.getEvento()))
                .messageBody(mensajePublicar.getMensaje())
                .build();

    }


    private String definirEstrategia(PublicadoraMensajesSQS publicadoraMensajesSQS) {

        if (publicadoraMensajesSQS instanceof CapacidadEndeudamiento) {
            return ConstantesMensajesSQS.EVENTO_CALCULAR_ENDEUDAMIENTO;
        } else if (publicadoraMensajesSQS instanceof ReporteAprobarRechazar) {
            return ConstantesMensajesSQS.EVENTO_NOTIFICAR;
        }
        return null;
    }


    private Map<String, MessageAttributeValue> generarAtributos(String evento) {
        return Map.of(
                "evento", MessageAttributeValue.builder().dataType("String").stringValue(evento).build()
        );
    }
}
