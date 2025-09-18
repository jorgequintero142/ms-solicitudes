package co.com.crediya.usecase;

import java.math.BigDecimal;

public class Constantes {

    private Constantes() {
    }

    public static final int ESTADO_PENDIENTE = 1;
    public static final String ERROR_VALOR_MONTO = "Valor del monto no es correcto";
    public static final String ERROR_PLAZO = "Plazo no es correcto";
    public static final String ERROR_TIPO_PRESTAMO = "No existe tipo de prestamo";
    public static final String ERROR_CONSULTA_CLIENTE = "No se encontró información del cliente";
    public static final String ERROR_DE_COMUNICACION = "Error inesperado con la consulta a cliente";

    public static final String ERROR_INESPERADO = "Error inesperado";
    public static final String MENSAJE_ERROR_INESPERADO = "Intente más tarde";
    public static final int CODIGO_ERROR_INESPERADO = 409;

    public static final int PLAZO_MINIMO = 6;
    public static final BigDecimal MONTO_MINIMO = new BigDecimal("0");

    public static final String ERROR_DOCUMENTO_SOLICITUD = "El número de documento de la solicitud, no corresponde con el tuyo";
    public static final String ERROR_ROL_CREAR_SOLICITUD = "Con tu rol no se permite crear solicitudes";



    public static final String ESTADO_ENVIADO_NO_PERMITDO ="Estado enviado no es permitido";
}