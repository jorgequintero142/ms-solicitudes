package co.com.crediya.model.solicitud;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Estado {
    private int idEstado;
    private String nombre;
    private String descripcion;
}

