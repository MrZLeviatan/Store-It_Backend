package co.edu.uniquindio.dto.objects.contrato;

import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para registrar un nuevo contrato en el sistema.
 * <p>
 * Contiene la información necesaria para crear un contrato válido, como la fecha de finalización,
 * el valor del contrato, una descripción, así como las referencias al cliente, agente de ventas y espacio asignado.
 *
 * @param fechaFin     Fecha en la que finaliza el contrato.
 * @param valor        Valor económico total del contrato.
 * @param descripcion  Descripción detallada del contrato (máximo 500 caracteres).
 */
public record CrearContratoDto(

        @NotNull LocalDate fechaFin,
        @NotNull BigDecimal valor,
        @NotBlank @Length(max = 500) String descripcion,
        @NotNull String emailCliente,
        @NotNull Long idAgenteVentas,
        @NotNull Long idEspacio

) {
}
