package co.com.crediya.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RespuestaApi<T> {
    private int estado;
    private String mensaje;
    private T data;
}
