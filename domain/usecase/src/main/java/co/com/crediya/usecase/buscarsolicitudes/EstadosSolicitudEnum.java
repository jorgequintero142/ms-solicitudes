package co.com.crediya.usecase.buscarsolicitudes;

import java.util.Arrays;

public enum EstadosSolicitudEnum {
    PENDIENTE("PENDIENTE",1),
    APROBADA("APROBADA",2),
    REVISION("REVISION",3),
    RECHAZADA("RECHAZADA",4);

    private final String estado;
    private final int codigo;
    EstadosSolicitudEnum(String estado, int codigo) {
        this.estado = estado;
        this.codigo = codigo;
    }

    public static int buscarCodigo(String valor) {
         return Arrays
                .stream(EstadosSolicitudEnum.values())
                .filter(estadosSolicitudEnum -> estadosSolicitudEnum.estado.equals(valor))
                 .map(estadosSolicitudEnum -> estadosSolicitudEnum.codigo).findFirst().orElse(0);


    }


}
