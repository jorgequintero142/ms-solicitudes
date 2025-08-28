package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SolicitudEntity {
    @Id
    @Column("id_solicitud")
    private Integer idSolicitud;
    private BigDecimal monto;
    private int plazo;
    private String email;
    private String documentoIdentidad;
    private int idEstado;
    private int idTipoPrestamo;
}
