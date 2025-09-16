package co.com.crediya.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Table(name = "solicitud")
public class SolicitudBasicaEntity {
        @Id
        @Column(name = "id_solicitud")
        protected Integer idSolicitud;
        protected BigDecimal monto;
        protected int plazo;
        protected String email;
        @Column(name = "documento_identidad")
        protected String documentoIdentidad;
        protected String tipoPrestamo;
        protected String estado;
        protected BigDecimal tasaInteres;
        protected BigDecimal totalDeuda;
}
