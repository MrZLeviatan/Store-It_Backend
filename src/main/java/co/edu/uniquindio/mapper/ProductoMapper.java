package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.Producto.ProductoDto;
import co.edu.uniquindio.model.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {


    ProductoDto toDTO(Producto producto);

    Producto toEntity(ProductoDto productoDto);
}
