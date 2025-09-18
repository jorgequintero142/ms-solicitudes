package co.com.crediya.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder()
public class RespuestaGenericaApi {
    private int estado;
    private String mensaje;
}
