package co.com.crediya.model.solicitud;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private int idSolicitud;
    private BigDecimal monto;
    private int plazo;
    private String email;
    private String documentoIdentidad;
    private int idEstado;
    private int idTipoPrestamo;
}