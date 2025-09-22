package co.com.crediya.model.capacidadendeudamiento;

import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacidadEndeudamiento extends PublicadoraMensajesSQS {
   private BigDecimal salario;
   private BigDecimal sumatoriaDeudaMensual;
   private BigDecimal monto;
   private int plazo;
   private BigDecimal tasaInteres;
   private String email;
   private String nombre;
   private int idSoliciutd;
}
