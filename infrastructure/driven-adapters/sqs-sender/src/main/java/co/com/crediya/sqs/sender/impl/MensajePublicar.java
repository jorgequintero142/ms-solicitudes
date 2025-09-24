package co.com.crediya.sqs.sender.impl;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MensajePublicar {
    private String evento;
    private String mensaje;
}
