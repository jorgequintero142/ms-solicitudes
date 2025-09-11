package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Estado;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudCreada;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.dto.InformacionUsuario;
import co.com.crediya.model.solicitud.dto.InformacionUsuarioToken;
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
    private InformacionUsuarioToken informacionUsuarioToken;
    private InformacionUsuario informacionUsuario;


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
        estado.setIdEstado(1);
        estado.setNombre("Pendiente aprobación");

        tipoPrestamo = new TipoPrestamo();
        tipoPrestamo.setIdTipoPrestamo(1);
        tipoPrestamo.setNombre("Personal");

        usuarioResponse = new UsuarioResponse();
        datosUsuario = new DatosUsuario();
        datosUsuario.setEmail("mail@mail.com");
        usuarioResponse.setData(datosUsuario);


        informacionUsuario =InformacionUsuario.builder()
                        .rol("Cliente")
                .subject("mail@mail.com")
                .documento("10922123")
                .build();

       informacionUsuarioToken = InformacionUsuarioToken
               .builder()
               .mensaje("ok")
               .data(informacionUsuario)
               .estado(200)
               .build();


        when(clienteWebClientes.buscarUsuarioPorToken(anyString())).thenReturn(Mono.just(informacionUsuarioToken));
        when(tipoPrestamoRepository.buscarPorId(anyInt())).thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.buscarPorId(anyInt())).thenReturn(Mono.just(estado));

    }

    private Solicitud generarSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(6);
        solicitud.setMonto(BigDecimal.valueOf(400000));
        solicitud.setDocumentoIdentidad("10922123");
        solicitud.setIdTipoPrestamo(1);
        solicitud.setEmail("mail@mail.com");
        return solicitud;
    }

    private SolicitudCreada generarSolicitudCreada() {
        SolicitudCreada solicitudCreada = new SolicitudCreada();
        solicitudCreada.setPlazo(6);
        solicitudCreada.setMonto(BigDecimal.valueOf(400000));
        solicitudCreada.setDocumentoIdentidad("10922123");
        solicitudCreada.setEstado("Pendiente aprobación");
        solicitudCreada.setEmail("mail@mail.com");
        solicitudCreada.setTipoPrestamo("Personal");
        return solicitudCreada;
    }

    @Test
    void registrarConExito() {
        Solicitud solicitud = generarSolicitud();
        SolicitudCreada solicitudCreada = generarSolicitudCreada();
        when(solicitudRepository.registrar(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectNextMatches(u->
                        u.getEstado().equals(solicitudCreada.getEstado())
                )
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
        when(clienteWebClientes.buscarUsuarioPorToken(anyString())).thenReturn(Mono.error(new ParametroNoValidoException(Constantes.ERROR_CONSULTA_CLIENTE)));
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

    @Test
    void errorConPermisosRol() {
        Solicitud solicitud = generarSolicitud();
        when(solicitudRepository.registrar(any(Solicitud.class))).thenReturn(Mono.just(solicitud));
        informacionUsuario.setRol("RolError");
        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_ROL_CREAR_SOLICITUD);
                })
                .verify();
    }

    @Test
    void errorConPermisosDocumento() {
        Solicitud solicitud = generarSolicitud();
        solicitud.setDocumentoIdentidad("1234Fail");
        when(solicitudRepository.registrar(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        StepVerifier.create(registrarSolicitudUseCase.registrar(solicitud, ""))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_DOCUMENTO_SOLICITUD);
                })
                .verify();
    }
}
