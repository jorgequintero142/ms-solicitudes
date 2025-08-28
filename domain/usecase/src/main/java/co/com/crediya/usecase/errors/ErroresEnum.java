package co.com.crediya.usecase.errors;

public enum ErroresEnum {
    ERROR_VALOR_MONTO("Valor del monto no es correcto"),
    ERROR_PLAZO("Plazo no es correcto"),
    ERROR_TIPO_PRESTAMO("No existe tipo de prestamo"),
    ERROR_CONSULTA_CLIENTE("No se encontró información del cliente");
    private final String mensaje;


    ErroresEnum(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}