package co.com.crediya.model.solicitud.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class InformacionUsuarioToken {
    private String rol;
    private String documento;
    private String subject;
}
