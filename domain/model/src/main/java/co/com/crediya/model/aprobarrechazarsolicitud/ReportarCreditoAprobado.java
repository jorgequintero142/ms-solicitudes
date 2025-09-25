package co.com.crediya.model.aprobarrechazarsolicitud;


import co.com.crediya.model.gateways.PublicadoraMensajesSQS;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class ReportarCreditoAprobado extends PublicadoraMensajesSQS {
    private int totalPrestamosAprobados;
    private BigDecimal montoTotalPrestamos;
}
