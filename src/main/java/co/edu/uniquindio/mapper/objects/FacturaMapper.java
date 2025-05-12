package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.factura.FacturaDto;
import co.edu.uniquindio.model.objects.Factura;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring",uses = {DetalleFacturaMapper.class,
        QuantityAreaConverter.class})
public interface FacturaMapper {



    FacturaDto facturaToDto(Factura factura);


 //   List<FacturaDto> toFacturaDTOList(List<Factura> factura);

}
