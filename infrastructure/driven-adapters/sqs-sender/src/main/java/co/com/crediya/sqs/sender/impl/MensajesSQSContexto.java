package co.com.crediya.sqs.sender.impl;

import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MensajesSQSContexto  {
    private final MensajesSQSStrategy notificarStrategy;
    private final MensajesSQSStrategy endeudamientoStrategy;

    public MensajesSQSContexto(@Qualifier("NotificarDecisionStrategy") MensajesSQSStrategy notificarStrategy,
                               @Qualifier("ValidacionAutomaticaStrategy")MensajesSQSStrategy endeudamientoStrategy) {
        this.notificarStrategy = notificarStrategy;
        this.endeudamientoStrategy = endeudamientoStrategy;
       }

    public MensajesSQSStrategy obtenerEstrategia(String tipoEstrategia) {
        if (ConstantesMensajesSQS.EVENTO_NOTIFICAR.equals(tipoEstrategia)) {
            return notificarStrategy;
        } else if (ConstantesMensajesSQS.EVENTO_CALCULAR_ENDEUDAMIENTO.equals(tipoEstrategia)) {
            return endeudamientoStrategy;
        } else {
            throw new IllegalArgumentException("Unknown product type: " + tipoEstrategia);
        }
    }
}