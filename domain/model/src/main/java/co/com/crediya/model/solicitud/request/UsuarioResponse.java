package co.com.crediya.model.solicitud.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor // ✅ Necesario para Jackson
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioResponse {
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