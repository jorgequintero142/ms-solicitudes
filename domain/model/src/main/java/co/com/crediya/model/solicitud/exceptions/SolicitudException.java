package co.com.crediya.model.solicitud.exceptions;

import lombok.Getter;

@Getter
public abstract class SolicitudException extends Exception {
    protected final String error;
    protected final int codigoEstado;

    /**
     * Se definen estas constantes para que a nivel de
     * dominio no se importe las clases del paquete
     * http de spring
     */
    static final int ESTADO_NO_ENCONTRADO = 404;
    static final int ESTADO_ERROR_PARAMETRO = 400;

    SolicitudException(String error, String mensaje, int codigoEstado) {
        super(mensaje);
        this.error = error;
        this.codigoEstado = codigoEstado;
    }
}
