package ec.nttdata.challenge2.dao;

import ec.nttdata.challenge2.domain.Cuenta;
import ec.nttdata.challenge2.domain.Movimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ICuentaDao extends CrudRepository<Cuenta, String> {

    @Query("SELECT DISTINCT c " +
            "FROM Cuenta c " +
            "WHERE c.clienteId = :clienteId ")
    Cuenta getCuentaByClienteId(String clienteId);

}
