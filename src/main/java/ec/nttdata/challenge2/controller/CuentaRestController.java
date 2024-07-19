package ec.nttdata.challenge2.controller;

import ec.nttdata.challenge2.domain.Cuenta;
import ec.nttdata.challenge2.service.CuentaService;
import ec.nttdata.challenge2.utils.BindingUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuentas")
public class CuentaRestController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Cuenta cuenta, BindingResult result) {
        Cuenta createdCuenta;
        Map<String, Object> accountOwner;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            BindingUtils.handleBindingErrors(result, response);
        }

        try {
            accountOwner = cuentaService.getCuentaCliente(cuenta.getClienteId());
            if (accountOwner == null) {
                response.put("mensaje", "El cliente no existe");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            createdCuenta = cuentaService.save(cuenta);
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos, revise el ID del Cliente");
            response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La cuenta ha sido creada con éxito!");
        response.put("cuenta", createdCuenta);
        response.put("cliente", accountOwner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cuenta> readAll() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return cuentaService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable String id) {

        Cuenta cuenta;
        Map<String, Object> accountOwner;
        Map<String, Object> response = new HashMap<>();

        try {
            cuenta = cuentaService.read(id);
            accountOwner = cuentaService.getCuentaCliente(cuenta.getClienteId());
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cuenta == null) {
            response.put("mensaje", "La cuenta ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("cuenta", cuenta);
        responseMap.put("cliente", accountOwner);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Map<String, Object> requestBody, BindingResult result, @PathVariable String id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Cuenta cuentaActual;
        Cuenta updatedCuenta;
        Map<String, Object> accountOwner;

        Map<String, Object> response = new HashMap<>();

        try {
            cuentaActual = cuentaService.read(id);
            accountOwner = cuentaService.getCuentaCliente(cuentaActual.getClienteId());
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(result.hasErrors()) {
            BindingUtils.handleBindingErrors(result, response);
        }

        if (cuentaActual == null) {
            response.put("mensaje", "Error: no se pudo editar, La cuenta ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {

            cuentaActual.setTipo(requestBody.get("tipo").toString());
            cuentaActual.setEstado((Boolean) requestBody.get("estado"));

            updatedCuenta = cuentaService.save(cuentaActual);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La cuenta ha sido actualizado con éxito!");
        response.put("cuenta", updatedCuenta);
        response.put("cliente", accountOwner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {

        Map<String, Object> response = new HashMap<>();

        try {
            cuentaService.read(id);
            cuentaService.delete(id);
        } catch (DataAccessException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            response.put("mensaje", "Error al eliminar la cuenta de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La cuenta se ha eliminado con éxito!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
