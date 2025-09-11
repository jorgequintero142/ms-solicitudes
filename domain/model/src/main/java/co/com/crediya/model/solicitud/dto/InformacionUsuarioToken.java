package co.com.crediya.model.solicitud.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class InformacionUsuarioToken {
    private int estado;
    private String mensaje;
    private InformacionUsuario data;
}
