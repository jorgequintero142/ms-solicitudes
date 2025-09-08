package co.com.crediya.model.solicitud.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DatosUsuario {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private BigDecimal salarioBase;
}