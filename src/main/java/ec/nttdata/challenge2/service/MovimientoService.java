package ec.nttdata.challenge2.service;

import ec.nttdata.challenge2.dao.IMovimientoDao;
import ec.nttdata.challenge2.domain.Movimiento;
import ec.nttdata.challenge2.dto.MovimientoProjection;
import ec.nttdata.challenge2.utils.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoService extends CrudService<Movimiento, IMovimientoDao> {

    public List<MovimientoProjection> getEstadoCuenta(String cuentaId) {
        return dao.getEstadoCuenta(cuentaId);
    }

    public List<MovimientoProjection> getAllMovimientos() {
        return dao.getAllMovimientos();
    }

}
