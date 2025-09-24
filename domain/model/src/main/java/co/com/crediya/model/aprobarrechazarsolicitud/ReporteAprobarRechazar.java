package co.com.crediya.model.aprobarrechazarsolicitud;

import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteAprobarRechazar extends PublicadoraMensajesSQS {
    private String email;
    private String nombre;
    private String estado;
    private int idSolicitud;
}
