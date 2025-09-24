package co.com.crediya.model.solicitud;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Solicitud {
    private BigDecimal monto;
    private int plazo;
    private String documentoIdentidad;
    private int idTipoPrestamo;
    private String email;
    private boolean validacionAutomatica;
}