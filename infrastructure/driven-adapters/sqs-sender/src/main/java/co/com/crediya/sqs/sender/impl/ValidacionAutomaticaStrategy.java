package co.com.crediya.sqs.sender.impl;

import co.com.crediya.model.capacidadendeudamiento.CapacidadEndeudamiento;
import org.springframework.stereotype.Component;

@Component("ValidacionAutomaticaStrategy")
public class ValidacionAutomaticaStrategy implements  MensajesSQSStrategy<CapacidadEndeudamiento> {
    @Override
    public MensajePublicar crearMensajePublicar(CapacidadEndeudamiento capacidadEndeudamiento) {
        return MensajePublicar
                .builder()
                .mensaje(personalizarMensaje(capacidadEndeudamiento))
                .evento(ConstantesMensajesSQS.EVENTO_CALCULAR_ENDEUDAMIENTO)
                .build();
    }

    @Override
    public String personalizarMensaje(CapacidadEndeudamiento capacidadEndeudamiento) {
       return String.format(
                "%s,%s,%s,%d,%s,%s,%s,%d",
                capacidadEndeudamiento.getNombre(),
                capacidadEndeudamiento.getMonto(),
                capacidadEndeudamiento.getSalario(),
                capacidadEndeudamiento.getPlazo(),
                capacidadEndeudamiento.getTasaInteres(),
                capacidadEndeudamiento.getSumatoriaDeudaMensual(),
                capacidadEndeudamiento.getEmail(),
               capacidadEndeudamiento.getIdSoliciutd()

        );

    }
}