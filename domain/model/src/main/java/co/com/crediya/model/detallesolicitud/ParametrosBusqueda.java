package co.com.crediya.model.detallesolicitud;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametrosBusqueda {

    public String estados;
    public List<Integer> codigosEstado;
    public int totalRegistros;
    public int pagina;

}
