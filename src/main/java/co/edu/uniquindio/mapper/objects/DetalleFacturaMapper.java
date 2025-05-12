package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.detalleFactura.DetalleFacturaDto;
import co.edu.uniquindio.model.objects.DetalleFactura;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {QuantityAreaConverter.class})
public interface DetalleFacturaMapper {


    DetalleFacturaDto toDto(DetalleFactura detalleFactura);

}
