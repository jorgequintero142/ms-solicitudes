package co.com.crediya.model.solicitud.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioResponse {
    private Integer estado;
    private String mensaje;
    private DatosUsuario data;

}