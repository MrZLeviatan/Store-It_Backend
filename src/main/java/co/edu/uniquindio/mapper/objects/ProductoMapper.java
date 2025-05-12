package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.producto.CrearProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.model.objects.Producto;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        QuantityAreaConverter.class})
public interface ProductoMapper {



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoProducto", constant = "EN_BODEGA")
    @Mapping(target = "historialMovimientos",expression = "java(new java.util.ArrayList<>())")
    @Mapping(source = "areaOcupada", target = "areaOcupada", qualifiedByName = "toQuantity")
    Producto toEntity(CrearProductoDto productoDto);


    @Mapping(source = "areaOcupada", target = "areaOcupada", qualifiedByName = "toDouble")
    ProductoDto toDto(Producto producto);


}
