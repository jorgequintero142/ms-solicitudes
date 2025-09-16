package co.com.crediya.model.detallesolicitud;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class InformacionClienteDTO {
    private String nombre;
    private BigDecimal salarioBase;
}
