package co.com.crediya.usecase.calcularcapacidadendeudamiento;

import co.com.crediya.model.capacidadendeudamiento.TotalDeudaMensual;
import co.com.crediya.model.capacidadendeudamiento.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.model.gateways.PublicadorSQSService;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalcularCapacidadEndeudamientoUseCaseTest {

    private CalcularCapacidadEndeudamientoUseCase useCase;
    private CapacidadEndeudamientoRepository capacidadEndeudamientoRepository;
    private PublicadorSQSService publicadorSQSService;

    @BeforeEach
    void setUp() {
        capacidadEndeudamientoRepository = Mockito.mock(CapacidadEndeudamientoRepository.class);
        publicadorSQSService = Mockito.mock(PublicadorSQSService.class);
        useCase = new CalcularCapacidadEndeudamientoUseCase(publicadorSQSService, capacidadEndeudamientoRepository);
    }

    private Solicitud generarSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(6);
        solicitud.setMonto(BigDecimal.valueOf(400000));
        solicitud.setDocumentoIdentidad("10922123");
        return solicitud;
    }

    private DatosUsuario generarDatosUsuario() {
        DatosUsuario data = new DatosUsuario();
        data.setEmail("mail@mail.com");
        data.setNombre("Jorge");
        data.setSalarioBase(BigDecimal.valueOf(2000000));
        return data;
    }

    @Test
    void calcularCapacidadEndeudamientoExito() {
        Solicitud solicitud = generarSolicitud();
        DatosUsuario data = generarDatosUsuario();

        when(capacidadEndeudamientoRepository.calcularDeudaMensual(any()))
                .thenReturn(Mono.just(new TotalDeudaMensual(BigDecimal.valueOf(100000))));

        when(publicadorSQSService.send(any()))
                .thenReturn(Mono.just("Mensaje enviado"));

        StepVerifier.create(useCase.calcularCapacidadEndeudamiento(solicitud, data, BigDecimal.valueOf(0.1), 1))
                .expectNext("Mensaje enviado")
                .verifyComplete();
    }
}
