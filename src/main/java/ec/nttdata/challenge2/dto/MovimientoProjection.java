package ec.nttdata.challenge2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MovimientoProjection {
    String getId();
    LocalDate getFecha();
    String getCuentaId();
    String getTipo();
    BigDecimal getValor();
    BigDecimal getSaldoPosterior();
}
