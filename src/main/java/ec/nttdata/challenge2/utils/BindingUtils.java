package ec.nttdata.challenge2.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BindingUtils {

    public static ResponseEntity<Map<String, Object>> handleBindingErrors(BindingResult bindingResult, Map<String, Object> response) {
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                .collect(Collectors.toList());

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
