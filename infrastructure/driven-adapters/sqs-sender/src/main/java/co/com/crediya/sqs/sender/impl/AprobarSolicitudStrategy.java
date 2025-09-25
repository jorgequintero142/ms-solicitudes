package co.com.crediya.sqs.sender.impl;

import co.com.crediya.model.aprobarrechazarsolicitud.ReportarCreditoAprobado;
import org.springframework.stereotype.Component;

@Component("AprobarSolicitudStrategy")
public class AprobarSolicitudStrategy implements MensajesSQSStrategy<ReportarCreditoAprobado> {
    @Override
    public MensajePublicar crearMensajePublicar(ReportarCreditoAprobado reportarCreditoAprobado) {
        return MensajePublicar
                .builder()
                .evento(ConstantesMensajesSQS.EVENTO_APROBAR)
                .mensaje(personalizarMensaje(reportarCreditoAprobado))
                .build();
    }

    @Override
    public String personalizarMensaje(ReportarCreditoAprobado reportarCreditoAprobado) {
        return String.format(
                "%d,%s",
                1,
                reportarCreditoAprobado.getMontoTotalPrestamos()
        );
    }
}
