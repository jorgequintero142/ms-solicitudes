package co.com.crediya.model.solicitud;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SolicitudCreada {

    private String estado;
    private String tipoPrestamo;
    private BigDecimal monto;
    private int plazo;
    private String documentoIdentidad;
    private String email;
}
