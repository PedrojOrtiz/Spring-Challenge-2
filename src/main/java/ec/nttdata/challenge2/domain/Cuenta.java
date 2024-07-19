package ec.nttdata.challenge2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
public class Cuenta implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "cuenta_id")
    private String id;

    @Column(name = "cuenta_cliente_id", unique = true, length = 36)
    private String clienteId;

    @Column(name = "cuenta_numero", unique = true, length = 10)
    private String numero;

    @Column(name = "cuenta_tipo", length = 10)
    private String tipo;

    @Column(name = "cuenta_saldo", precision = 24, scale = 14)
    private BigDecimal saldo;

    @Column(name = "cuenta_estado", columnDefinition = "boolean DEFAULT true")
    private Boolean estado;

}
