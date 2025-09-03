package co.com.crediya.model.solicitud.exceptions;

public class ParametroNoValidoException extends SolicitudException {
    static final String MENSAJE = "Validación fallida";

    public ParametroNoValidoException(String error) {
        super(error, ParametroNoValidoException.MENSAJE, SolicitudException.ESTADO_ERROR_PARAMETRO);
    }
}
