package ec.nttdata.challenge2.dao;

import ec.nttdata.challenge2.domain.Movimiento;
import ec.nttdata.challenge2.dto.MovimientoProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMovimientoDao extends CrudRepository<Movimiento, String> {

    @Query(value = "SELECT DISTINCT " +
            "mov.movimiento_id AS id, " +
            "mov.movimiento_fecha AS fecha, " +
            "mov.movimiento_cuenta_id AS cuentaId, " +
            "mov.movimiento_tipo AS tipo, " +
            "mov.movimiento_valor AS valor, " +
            "mov.movimiento_saldo_posterior AS saldoPosterior " +
            "FROM Movimiento mov " +
            "WHERE mov.movimiento_cuenta_id = :cuentaId " +
            "ORDER BY mov.movimiento_fecha", nativeQuery = true)
    List<MovimientoProjection> getEstadoCuenta(String cuentaId);

    @Query(value = "SELECT DISTINCT " +
            "mov.movimiento_id AS id, " +
            "mov.movimiento_fecha AS fecha, " +
            "mov.movimiento_cuenta_id AS cuentaId, " +
            "mov.movimiento_tipo AS tipo, " +
            "mov.movimiento_valor AS valor, " +
            "mov.movimiento_saldo_posterior AS saldoPosterior " +
            "FROM Movimiento mov " +
            "ORDER BY mov.movimiento_fecha", nativeQuery = true)
    List<MovimientoProjection> getAllMovimientos();

}
