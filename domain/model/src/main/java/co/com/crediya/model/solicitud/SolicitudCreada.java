package co.com.crediya.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCreada extends Solicitud {

    private int idEstado;
    private String estado;
    private String tipoPrestamo;


}
