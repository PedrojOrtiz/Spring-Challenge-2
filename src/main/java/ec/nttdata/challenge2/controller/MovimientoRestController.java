package ec.nttdata.challenge2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ec.nttdata.challenge2.domain.Cuenta;
import ec.nttdata.challenge2.domain.Movimiento;
import ec.nttdata.challenge2.dto.EstadoCuentaDTO;
import ec.nttdata.challenge2.dto.MovimientoProjection;
import ec.nttdata.challenge2.service.CuentaService;
import ec.nttdata.challenge2.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")
public class MovimientoRestController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> requestBody) {
        Movimiento createdMovimiento;
        Cuenta accountAssociated;
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String cuentaId = requestBody.get("cuentaId").toString();
        requestBody.remove("cuentaId");
        Movimiento movimiento = mapper.convertValue(requestBody, Movimiento.class);
        try {
            accountAssociated = cuentaService.read(cuentaId);
            movimiento.setCuenta(accountAssociated);
            //Deposito
            if (movimiento.getValor().signum() > 0) {
                accountAssociated.setSaldo(accountAssociated.getSaldo().add(movimiento.getValor()));
            }
            //Retiro
            else {
                //Saldo Suficiente
                if (movimiento.getValor().abs().compareTo(accountAssociated.getSaldo()) < 0) {
                    accountAssociated.setSaldo(accountAssociated.getSaldo().subtract(movimiento.getValor().abs()));
                }
                //Saldo Insuficiente
                else {
                    response.put("mensaje", "Saldo Insuficiente");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            movimiento.setSaldoPosterior(accountAssociated.getSaldo());
            cuentaService.save(accountAssociated);
            createdMovimiento = movimientoService.save(movimiento);
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos, revise el ID del Cliente");
            response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El movimiento ha sido registrado con éxito!");
        response.put("movimiento", createdMovimiento);
        response.put("cuenta", accountAssociated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<MovimientoProjection> readAll() {
        return movimientoService.getAllMovimientos();
    }

    @GetMapping("/estadocuenta/{cuentaId}")
    public EstadoCuentaDTO getEstadoCuenta(@PathVariable String cuentaId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EstadoCuentaDTO ec = new EstadoCuentaDTO();
        ec.setMovimientoList(movimientoService.getEstadoCuenta(cuentaId));
        Cuenta accountAssociated = cuentaService.read(cuentaId);
        ec.setCuenta(accountAssociated);
        ec.setCliente(cuentaService.getCuentaCliente(accountAssociated.getClienteId()));
        return ec;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable String id) {

        Movimiento movimiento;
        Map<String, Object> response = new HashMap<>();

        try {
            movimiento = movimientoService.read(id);
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (movimiento == null) {
            response.put("mensaje", "El movimiento ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(movimiento, HttpStatus.OK);
    }




}
