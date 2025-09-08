package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Estado;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.exceptions.ParametroNoValidoException;
import co.com.crediya.model.solicitud.gateways.ClienteWebClientes;
import co.com.crediya.model.solicitud.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.model.solicitud.dto.DatosUsuario;
import co.com.crediya.model.solicitud.dto.UsuarioResponse;
import co.com.crediya.usecase.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrarSolicitudTest {

    private RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private SolicitudRepository solicitudRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private EstadoRepository estadoRepository;
    private ClienteWebClientes clienteWebClientes;
    private UsuarioResponse usuarioResponse;
    private DatosUsuario datosUsuario;


    private Estado estado;
    private TipoPrestamo tipoPrestamo;

    @BeforeEach
    void setUp() {
        solicitudRepository = Mockito.mock(SolicitudRepository.class);
        tipoPrestamoRepository = Mockito.mock(TipoPrestamoRepository.class);
        estadoRepository = Mockito.mock(EstadoRepository.class);
        clienteWebClientes = Mockito.mock(ClienteWebClientes.class);
        registrarSolicitudUseCase = new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository, estadoRepository, clienteWebClientes);

        estado = new Estado();
        estado.setDescripcion("Pendiente");
        estado.setIdEstado(1);
        estado.setDescripcion("Pendiente aprobación");

        tipoPrestamo = new TipoPrestamo();
        tipoPrestamo.setIdTipoPrestamo(1);
        tipoPrestamo.setNombre("Personal");

        usuarioResponse = new UsuarioResponse();
        datosUsuario = new DatosUsuario();
        datosUsuario.setEmail("mail@mail.com");
        usuarioResponse.setData(datosUsuario);

        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.just(usuarioResponse));
        when(tipoPrestamoRepository.buscarPorId(anyInt())).thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.buscarPorId(anyInt())).thenReturn(Mono.just(estado));

    }

    private Solicitud generarSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(6);
        solicitud.setMonto(BigDecimal.valueOf(400000));
        solicitud.setDocumentoIdentidad("10922123");
        solicitud.setIdTipoPrestamo(1);
        return solicitud;
    }

    private SolicitudCreada generarSolicitudCreada() {
        SolicitudCreada solicitudCreada = new SolicitudCreada();
        solicitudCreada.setPlazo(6);
        solicitudCreada.setMonto(BigDecimal.valueOf(400000));
        solicitudCreada.setDocumentoIdentidad("10922123");
        solicitudCreada.setIdTipoPrestamo(1);
        solicitudCreada.setEstado("Pendiente");
        solicitudCreada.setTipoPrestamo("Personal");
        return solicitudCreada;
    }

    @Test
    void registrarConExito() {
        Solicitud solicitud = generarSolicitud();
        SolicitudCreada solicitudCreada = generarSolicitudCreada();
        when(solicitudRepository.registrar(any(Solicitud.class))).thenReturn(Mono.just(solicitudCreada));

        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectNext(solicitudCreada)
                .verifyComplete();
        verify(solicitudRepository).registrar(solicitud);
    }

    @Test
    void errorTipoPrestamoNoExiste() {
        Solicitud solicitud = generarSolicitud();

        when(tipoPrestamoRepository.buscarPorId(anyInt())).thenReturn(Mono.empty());
        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_TIPO_PRESTAMO);
                })
                .verify();
    }

    @Test
    void errorClienteNoEncontrado() {
        Solicitud solicitud = generarSolicitud();

        when(tipoPrestamoRepository.buscarPorId(anyInt())).thenReturn(Mono.just(tipoPrestamo));
        when(clienteWebClientes.buscarCliente(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_CONSULTA_CLIENTE);
                })
                .verify();
    }


    @Test
    void errorConElPlazo() {
        Solicitud solicitud = generarSolicitud();
        solicitud.setPlazo(4);

        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_PLAZO);
                })
                .verify();
    }

    @Test
    void errorConElMonto() {
        Solicitud solicitud = generarSolicitud();
        solicitud.setMonto(new BigDecimal("0"));

        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_VALOR_MONTO);
                })
                .verify();
    }
}
