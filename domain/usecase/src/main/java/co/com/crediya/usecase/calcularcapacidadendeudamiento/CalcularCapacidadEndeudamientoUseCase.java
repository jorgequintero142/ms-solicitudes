package co.com.crediya.usecase.calcularcapacidadendeudamiento;

import co.com.crediya.model.capacidadendeudamiento.CapacidadEndeudamiento;
import co.com.crediya.model.capacidadendeudamiento.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.model.gateways.PublicadorSQSService;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class CalcularCapacidadEndeudamientoUseCase {

    private final PublicadorSQSService publicadorSQSService;
    private final CapacidadEndeudamientoRepository capacidadEndeudamientoRepository;

    public Mono<String> calcularCapacidadEndeudamiento(Solicitud solicitud, DatosUsuario data, BigDecimal tasaInteres, int idSolicitud) {
        return capacidadEndeudamientoRepository
                .calcularDeudaMensual(solicitud.getDocumentoIdentidad())
                .map(totalDeuda -> CapacidadEndeudamiento.builder()
                        .monto(solicitud.getMonto())
                        .plazo(solicitud.getPlazo())
                        .salario(data.getSalarioBase())
                        .email(data.getEmail())
                        .nombre(data.getNombre())
                        .tasaInteres(tasaInteres)
                        .sumatoriaDeudaMensual(totalDeuda.getTotalDeudaMensual())
                        .idSoliciutd(idSolicitud)
                        .build()
                )
                .flatMap(publicadorSQSService::send)
                .onErrorMap(e -> new ParametroNoValidoException(e.getMessage()));
    }
}
