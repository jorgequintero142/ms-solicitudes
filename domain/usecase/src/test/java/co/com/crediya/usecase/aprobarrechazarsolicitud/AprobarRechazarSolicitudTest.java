package co.com.crediya.usecase.aprobarrechazarsolicitud;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import co.com.crediya.model.aprobarrechazarsolicitud.gateways.AprobarRechazarSolicitudService;
import co.com.crediya.model.gateways.PublicadorSQSService;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AprobarRechazarSolicitudTest {

    private AprobarRechazarSolicitudUseCase aprobarRechazarSolicitudUseCase;
    private  ClienteWebClientes clienteWebClientes;
    private  SolicitudRepository solicitudRepository;
    private PublicadorSQSService publicadorSQSService;
    private final String documentoCliente =  "1234455";
    private final int CODIGO_SOLICITUD =123;
    private final String ESTADO ="APROBADA";
    private final String mensaje ="jorge, 123,APROBADA, jorge@mail.com";
    private static final String PROCESO_OK = "Se ha procesado la solicitud de manera exitosa";

    DatosUsuario datosUsuario = DatosUsuario.builder()
            .documentoIdentidad(documentoCliente)
            .apellido("quintero")
            .salarioBase(BigDecimal.valueOf(99999999))
            .nombre("jorge")
            .build();
    UsuarioResponse usuarioResponse = UsuarioResponse
            .builder()
            .data(datosUsuario)
            .build();


    private Solicitud generarSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(6);
        solicitud.setMonto(BigDecimal.valueOf(400000));
        solicitud.setDocumentoIdentidad("10922123");
        solicitud.setIdTipoPrestamo(1);
        solicitud.setEmail("mail@mail.com");
        return solicitud;
    }


    @BeforeEach
    void setUp() {
        solicitudRepository = Mockito.mock(SolicitudRepository.class);
        clienteWebClientes = Mockito.mock(ClienteWebClientes.class);
        publicadorSQSService = Mockito.mock(PublicadorSQSService.class);
        aprobarRechazarSolicitudUseCase = new AprobarRechazarSolicitudUseCase(publicadorSQSService, solicitudRepository,clienteWebClientes);
    }


    @Test
    void cambioEstadoOk() {

        Solicitud solicitud = generarSolicitud();
        when(solicitudRepository.aprobarRechazar(anyInt(), anyInt())).thenReturn(Mono.just(solicitud));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.just(usuarioResponse));
        when(publicadorSQSService.send(any(ReporteAprobarRechazar.class))).thenReturn(Mono.just(mensaje));
        StepVerifier.create(aprobarRechazarSolicitudUseCase.aprobarRechazar(CODIGO_SOLICITUD, ESTADO))
                .expectNext(PROCESO_OK)
                .verifyComplete();

        verify(solicitudRepository).aprobarRechazar(anyInt(), anyInt());
    }
}
