package co.edu.uniquindio.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.measure.Quantity;
import javax.measure.quantity.Area;

import org.mapstruct.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 * Conversor JPA para atributos de tipo {@link Quantity}&lt;{@link Area}&gt;.
 * <p>
 * Este conversor transforma valores de área en objetos {@code Quantity<Area>} a {@code Double} para persistencia
 * en la base de datos, y viceversa. Todos los valores se manejan en unidades de metros cuadrados (m²).
 * </p>
 *
 * <p>
 * Se utiliza con anotación {@code @Convert} o de manera automática gracias a {@code autoApply = true}.
 * </p>
 *
 * <p><b>Uso:</b> para atributos como área total, área disponible, etc.</p>
 *
 * @author MrZ.Leviatan
 */
@Converter(autoApply = true)
@Component
public class QuantityAreaConverter implements AttributeConverter<Quantity<Area>, Double> {

    /**
     * Convierte un objeto {@code Quantity<Area>} a {@code Double} para almacenar en la base de datos.
     * <p>La unidad utilizada es el metro cuadrado (m²).</p>
     *
     * @param attribute el valor de tipo {@code Quantity<Area>} a convertir.
     * @return el valor como {@code Double} en m², o {@code null} si el atributo es nulo.
     */
    @Override
    public Double convertToDatabaseColumn(Quantity<Area> attribute) {
        if (attribute == null) return null;
        return attribute.to(Units.SQUARE_METRE).getValue().doubleValue();
    }


    /**
     * Convierte un valor {@code Double} de la base de datos a un objeto {@code Quantity<Area>}.
     * <p>El valor se interpreta como metros cuadrados (m²).</p>
     *
     * @param dbData el valor de la base de datos.
     * @return una instancia de {@code Quantity<Area>} con unidad m², o {@code null} si el valor es nulo.
     */
    @Override
    public Quantity<Area> convertToEntityAttribute(Double dbData) {
        if (dbData == null) return null;
        return Quantities.getQuantity(dbData, Units.SQUARE_METRE);
    }

    // --- Métodos estáticos para MapStruct ---

    /**
     * Convierte un valor {@code Double} en una instancia de {@code Quantity<Area>}, en metros cuadrados (m²).
     *
     * @param value Valor en metros cuadrados.
     * @return Cantidad con unidad de área (m²).
     */
    @Named("toQuantity")
    public static Quantity<Area> toQuantity(Double value) {
        if (value == null) return null;
        return Quantities.getQuantity(value, Units.SQUARE_METRE);
    }


    /**
     * Convierte un {@code Quantity<Area>} a {@code Double} expresado en metros cuadrados (m²).
     *
     * @param quantity Cantidad de área.
     * @return Valor numérico en m².
     */
    @Named("toDouble")
    public static Double toDouble(Quantity<Area> quantity) {
        if (quantity == null) return null;
        return quantity.to(Units.SQUARE_METRE).getValue().doubleValue();
    }
}

