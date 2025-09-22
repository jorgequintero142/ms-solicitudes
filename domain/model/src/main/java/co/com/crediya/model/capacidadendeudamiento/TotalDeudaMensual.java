package co.com.crediya.model.capacidadendeudamiento;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TotalDeudaMensual {
    private BigDecimal totalDeudaMensual;
}
