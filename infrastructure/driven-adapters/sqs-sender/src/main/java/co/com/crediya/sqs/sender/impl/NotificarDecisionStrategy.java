package co.com.crediya.sqs.sender.impl;

import co.com.crediya.model.aprobarrechazarsolicitud.ReporteAprobarRechazar;
import org.springframework.stereotype.Component;


@Component("NotificarDecisionStrategy")
public class NotificarDecisionStrategy implements  MensajesSQSStrategy<ReporteAprobarRechazar>{
    @Override
    public MensajePublicar crearMensajePublicar(ReporteAprobarRechazar reporteAprobarRechazar) {
        return MensajePublicar
                .builder()
                .evento(ConstantesMensajesSQS.EVENTO_NOTIFICAR)
                .mensaje(personalizarMensaje(reporteAprobarRechazar))
                .build();
    }

    @Override
    public String personalizarMensaje(ReporteAprobarRechazar reporteAprobarRechazar) {
       return String.format(
                "%s,%d,%s,%s",
                reporteAprobarRechazar.getNombre(),
                reporteAprobarRechazar.getIdSolicitud(),
                reporteAprobarRechazar.getEstado(),
                reporteAprobarRechazar.getEmail()
        );
    }
}
