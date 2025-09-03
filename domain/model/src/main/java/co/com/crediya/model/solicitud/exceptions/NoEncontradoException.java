package co.com.crediya.model.solicitud.exceptions;

public class NoEncontradoException extends SolicitudException {
    static final String MENSAJE = "No encontrado";

    public NoEncontradoException(String error) {
        super(error, NoEncontradoException.MENSAJE, SolicitudException.ESTADO_NO_ENCONTRADO);
    }
}
