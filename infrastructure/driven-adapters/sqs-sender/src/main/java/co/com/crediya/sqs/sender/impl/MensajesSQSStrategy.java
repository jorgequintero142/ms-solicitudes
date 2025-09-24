package co.com.crediya.sqs.sender.impl;

import co.com.crediya.model.gateways.PublicadoraMensajesSQS;

public interface MensajesSQSStrategy<T extends PublicadoraMensajesSQS> {
    MensajePublicar crearMensajePublicar(T publicadoraMensajesSQS);
    String personalizarMensaje(T publicadoraMensajesSQS);
}
