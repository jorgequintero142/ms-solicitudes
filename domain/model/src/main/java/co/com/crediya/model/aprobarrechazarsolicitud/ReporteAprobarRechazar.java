package co.com.crediya.model.aprobarrechazarsolicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteAprobarRechazar {
    private String email;
    private String nombre;
    private String estado;
    private int idSolicitud;
}
