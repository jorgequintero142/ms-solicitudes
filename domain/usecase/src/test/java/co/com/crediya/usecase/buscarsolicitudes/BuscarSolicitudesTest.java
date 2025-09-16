package co.com.crediya.usecase.buscarsolicitudes;


import co.com.crediya.model.detallesolicitud.DetalleSolicitudDTO;
import co.com.crediya.model.detallesolicitud.ParametrosBusqueda;
import co.com.crediya.model.detallesolicitud.gateways.DetalleSolicitudRepository;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.usecase.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class BuscarSolicitudesTest {
    private BuscarSolicitudesUseCase  buscarSolicitudesUseCase;
    private  DetalleSolicitudRepository detalleSolicitudRepository;
    private ParametrosBusqueda parametrosBusqueda = ParametrosBusqueda
            .builder()
            .totalRegistros(10)
            .pagina(1)
            .estados("PENDIENTE")
            .build();

    private ClienteWebClientes clienteWebClientes;
    private final String documentoCliente =  "1234455";
    DetalleSolicitudDTO detalleSolicitudDTO =DetalleSolicitudDTO
            .builder()
            .email("mail@mail.com")
            .plazo(10)
            .monto(BigDecimal.valueOf(1000000))
            .totalSolicitudesAprobadas(BigDecimal.ZERO)
            .documentoIdentidad(documentoCliente)
            .totalSolicitudesAprobadas(BigDecimal.ONE)
            .build();

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

    @BeforeEach
    void setUp() {
        detalleSolicitudRepository = Mockito.mock(DetalleSolicitudRepository.class);
        clienteWebClientes = Mockito.mock(ClienteWebClientes.class);

        buscarSolicitudesUseCase = new BuscarSolicitudesUseCase(detalleSolicitudRepository, clienteWebClientes);



    }

    @Test
    void busquedaExitosa() {
        when(detalleSolicitudRepository.buscarSolicitudes(any())).thenReturn(Flux.just(detalleSolicitudDTO));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.just(usuarioResponse));
        StepVerifier.create(buscarSolicitudesUseCase.buscarSolicitudes(parametrosBusqueda))
                .expectNextMatches(u->
                            u.getDocumentoIdentidad().equals(documentoCliente)
                )
                .verifyComplete();
        verify(detalleSolicitudRepository).buscarSolicitudes(parametrosBusqueda);
    }

    @Test
    void busquedaValorDeudaMayorCero() {
        when(detalleSolicitudRepository.buscarSolicitudes(any())).thenReturn(Flux.just(detalleSolicitudDTO));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.just(usuarioResponse));
        StepVerifier.create(buscarSolicitudesUseCase.buscarSolicitudes(parametrosBusqueda))
                .expectNextMatches(u->
                        u.getTotalSolicitudesAprobadas().compareTo(BigDecimal.ZERO) == 1
                )
                .verifyComplete();
        verify(detalleSolicitudRepository).buscarSolicitudes(parametrosBusqueda);
    }


    @Test
    void busquedaErrorConsultaCliente() {
        when(detalleSolicitudRepository.buscarSolicitudes(any())).thenReturn(Flux.just(detalleSolicitudDTO));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn (Mono.empty());

        StepVerifier.create(buscarSolicitudesUseCase.buscarSolicitudes(parametrosBusqueda))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_CONSULTA_CLIENTE);
                })
                .verify();

    }

    @Test
    void busquedaErrorInvocandoServicio() {
        when(detalleSolicitudRepository.buscarSolicitudes(any())).thenReturn(Flux.just(detalleSolicitudDTO));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.error(new ParametroNoValidoException(Constantes.ERROR_DE_COMUNICACION)));

        StepVerifier.create(buscarSolicitudesUseCase.buscarSolicitudes(parametrosBusqueda))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_DE_COMUNICACION);
                })
                .verify();

    }
}
