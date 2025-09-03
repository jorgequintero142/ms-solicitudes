package co.com.crediya.usecase;

import java.math.BigDecimal;

public class Constantes {

    private Constantes() {
    }

    public static final String ERROR_VALOR_MONTO = "Valor del monto no es correcto";
    public static final String ERROR_PLAZO = "Plazo no es correcto";
    public static final String ERROR_TIPO_PRESTAMO = "No existe tipo de prestamo";
    public static final String ERROR_CONSULTA_CLIENTE = "No se encontró información del cliente";


    public static final String ERROR_INESPERADO = "Error inesperado";
    public static final String MENSAJE_ERROR_INESPERADO = "Intente más tarde";
    public static final int CODIGO_ERROR_INESPERADO = 409;

    public static final int PLAZO_MINIMO = 6;
    public static final BigDecimal MONTO_MINIMO = new BigDecimal("0");

}