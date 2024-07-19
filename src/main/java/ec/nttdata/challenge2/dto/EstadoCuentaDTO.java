package ec.nttdata.challenge2.dto;

import ec.nttdata.challenge2.domain.Cuenta;
import ec.nttdata.challenge2.domain.Movimiento;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EstadoCuentaDTO implements Serializable {

    @UuidGenerator
    private String codigo;

    private List<MovimientoProjection> movimientoList;

    private Cuenta cuenta;

    private Map<String, Object> cliente;

}
