package ec.nttdata.challenge2.service;

import ec.nttdata.challenge2.dao.ICuentaDao;
import ec.nttdata.challenge2.domain.Cuenta;
import ec.nttdata.challenge2.utils.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class CuentaService extends CrudService<Cuenta, ICuentaDao> {

    public Cuenta getCuentaByClienteId(String clienteId) {
        return dao.getCuentaByClienteId(clienteId);
    }

    public Map<String, Object> getCuentaCliente(String clienteId) {
        String url = "http://localhost:8080/clientes/".concat(clienteId);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Map.class);
    }

}
