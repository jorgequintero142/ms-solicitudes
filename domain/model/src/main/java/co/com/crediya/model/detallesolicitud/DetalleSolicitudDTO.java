package co.com.crediya.model.detallesolicitud;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class DetalleSolicitudDTO {
    private BigDecimal monto;
    private Integer plazo;
    private String email;
    private String documentoIdentidad;
    private String tipoPrestamo;
    private String estado;
    private BigDecimal tasaInteres;
    private String nombre;
    private BigDecimal salarioBase;
    private BigDecimal totalSolicitudesAprobadas;
}
