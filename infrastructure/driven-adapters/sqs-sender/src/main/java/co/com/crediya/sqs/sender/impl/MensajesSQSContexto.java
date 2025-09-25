package co.com.crediya.sqs.sender.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MensajesSQSContexto {
    private final MensajesSQSStrategy notificarStrategy;
    private final MensajesSQSStrategy endeudamientoStrategy;
    private final MensajesSQSStrategy aprobarStrategy;

    public MensajesSQSContexto(@Qualifier("NotificarDecisionStrategy") MensajesSQSStrategy notificarStrategy,
                               @Qualifier("ValidacionAutomaticaStrategy") MensajesSQSStrategy endeudamientoStrategy,
                               @Qualifier("AprobarSolicitudStrategy") MensajesSQSStrategy aprobarStrategy) {
        this.notificarStrategy = notificarStrategy;
        this.endeudamientoStrategy = endeudamientoStrategy;
        this.aprobarStrategy = aprobarStrategy;
    }

    public MensajesSQSStrategy obtenerEstrategia(String tipoEstrategia) {
        if (ConstantesMensajesSQS.EVENTO_NOTIFICAR.equals(tipoEstrategia)) {
            return notificarStrategy;
        } else if (ConstantesMensajesSQS.EVENTO_CALCULAR_ENDEUDAMIENTO.equals(tipoEstrategia)) {
            return endeudamientoStrategy;
        } else if (ConstantesMensajesSQS.EVENTO_APROBAR.equals(tipoEstrategia)) {
            return aprobarStrategy;
        } else {
            throw new IllegalArgumentException("Tipo desconocido: " + tipoEstrategia);
        }
    }
}