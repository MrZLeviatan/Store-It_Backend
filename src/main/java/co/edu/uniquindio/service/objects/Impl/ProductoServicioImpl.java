package co.edu.uniquindio.service.objects.Impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.CrearProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.EspacioMapper;
import co.edu.uniquindio.mapper.objects.ProductoMapper;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.Movimiento;
import co.edu.uniquindio.model.objects.Producto;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import co.edu.uniquindio.model.objects.enums.EstadoProducto;
import co.edu.uniquindio.model.objects.enums.TipoMovimiento;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.model.users.PersonalBodega;
import co.edu.uniquindio.repository.objects.ContratoRepo;
import co.edu.uniquindio.repository.objects.EspacioRepo;
import co.edu.uniquindio.repository.objects.MovimientoRepo;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.service.objects.ProductoServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServicioImpl implements ProductoServicio {

    private final ProductoRepo productoRepo;
    private final ClienteRepo clienteRepo;
    private final EspacioRepo espacioRepo;
    private final PersonalBodegaRepo personalBodegaRepo;
    private final MovimientoRepo movimientoRepo;
    private final ContratoRepo contratoRepo;
    private final ProductoMapper productoMapper;
    private final EmailServicio emailServicio;
    private final EspacioMapper espacioMapper;

    @Override
    public void crearProducto(CrearProductoDto crearProductoDTO) throws ElementoNoEncontradoException {

        Cliente cliente = clienteRepo.findByUser_Email(crearProductoDTO.emailCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        Espacio espacio = espacioRepo.findById(crearProductoDTO.idEspacio())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.ESPACIO_NO_ENCONTRADO));

        PersonalBodega personalBodega = personalBodegaRepo.findById(crearProductoDTO.idPersonalBodega())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Validar si el espacio está asignado al cliente mediante un contrato
        Optional<Contrato> contrato = contratoRepo.findByEspacioAndCliente(espacio, cliente);
        if (contrato.isEmpty()) {
            throw new ElementoNoEncontradoException("El espacio no está asignado a este cliente.");
        }

        Producto producto = productoMapper.toEntity(crearProductoDTO);

        espacio.agregarProducto(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setEspacio(espacio);
        movimiento.setPersonalResponsable(personalBodega);
        movimiento.setDetalle("Ingreso de producto al espacio");

        producto.getHistorialMovimientos().add(movimiento);
        cliente.getProductos().add(producto);
        personalBodega.getMovimientosProducto().add(movimiento);
        producto.setCliente(cliente);

        productoRepo.save(producto);
        movimientoRepo.save(movimiento);
        espacioRepo.save(espacio);
        personalBodegaRepo.save(personalBodega);
        clienteRepo.save(cliente);


        // Definimos el asunto del correo
        String asunto = "Confirmación de registro de producto - Store-It";

        // Definimos el cuerpo del mensaje
        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "¡Tu producto ha sido registrado exitosamente en Store-It!\n\n" +
                        "Detalles del producto:\n" +
                        "• Nombre: %s\n" +
                        "• Área ocupada: %.2f m²\n\n" +
                        "Este producto ha sido almacenado correctamente en el espacio asignado.\n\n" +
                        "Si tienes alguna duda o necesitas asistencia, no dudes en contactarnos.\n\n" +
                        "Gracias por confiar en nosotros.\n\n" +
                        "Saludos cordiales,\n" +
                        "El equipo de Store-It",
                cliente.getNombre(),
                producto.getNombre(),
                producto.getAreaOcupada()
                        .to(tech.units.indriya.unit.Units.SQUARE_METRE)
                        .getValue()
                        .doubleValue()
        );

        // Enviamos el correo electrónico
        enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);
    }


    private void enviarEmail(String emailDestinatario, String asunto, String cuerpo){
        // Se crea un DTO de email con su estructura para enviarse al receptor
        EmailDto emailDto = new EmailDto(emailDestinatario, asunto, cuerpo);
        // Se envía el email del destinatario.
        emailServicio.enviarCorreo(emailDto);
    }

    @Override
    public List<EspacioDto> listarEspaciosCliente(String email) throws ElementoNoEncontradoException {

        // Buscar el cliente por su email / Find client by email
        Cliente cliente = clienteRepo.findByUser_Email(email)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtener los contratos asociados al cliente / Get contracts associated to the client
        List<Contrato> contratos = contratoRepo.findByCliente(cliente);


        // Filtrar los espacios asociados a los contratos / Filter the spaces associated to the contracts
        List<Espacio> espacios = contratos.stream()
                .map(Contrato::getEspacio)  // Obtener el espacio de cada contrato / Get the space of each contract
                .collect(Collectors.toList());

        // Si no se encuentran espacios, lanzar excepción / If no spaces found, throw an exception
        if (espacios.isEmpty()) {
            throw new ElementoNoEncontradoException("No se encontraron espacios asociados a este cliente.");
        }

        return espacios.stream()
                .filter(espacio -> espacio.getEstadoEspacio().equals(EstadoEspacio.CONTRATADO_DISPONIBLE))
                .map(espacioMapper::toDto)  // Convertimos el espacio a EspacioDto / Convert the space to EspacioDto
                .collect(Collectors.toList());
    }


    @Override
    public void retirarProducto(RetiroProductoDto retirarProductoDTO) throws ElementoNoEncontradoException {

        Cliente cliente = clienteRepo.findByUser_Email(retirarProductoDTO.emailCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        Producto producto = cliente.getProductos().stream()
                .filter(p -> p.getId().equals(retirarProductoDTO.idProducto()))
                .findFirst()
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_ENCONTRADO));

        PersonalBodega personalBodega = personalBodegaRepo.findById(retirarProductoDTO.idPersonal())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        producto.setEstadoProducto(EstadoProducto.RETIRADO);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setEspacio(producto.getEspacio());
        movimiento.setPersonalResponsable(personalBodega);
        movimiento.setDetalle("Retiro del producto");

        producto.getHistorialMovimientos().add(movimiento);
        cliente.getProductos().add(producto);
        producto.setCliente(cliente);
        personalBodega.getMovimientosProducto().add(movimiento);
        Espacio espacio = producto.getEspacio();
        espacio.getProductos().remove(producto);

        movimientoRepo.save(movimiento);
        productoRepo.save(producto);
        espacioRepo.save(espacio);
        personalBodegaRepo.save(personalBodega);
        clienteRepo.save(cliente);


        // Definimos el asunto del correo / Define email subject
        String asunto = "Confirmación de retiro de producto - Store-It";

        // Definimos el cuerpo del mensaje / Define email body
        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "Te confirmamos que tu producto ha sido retirado exitosamente de Store-It.\n\n" +
                        "Detalles del producto retirado:\n" +
                        "• Nombre: %s\n" +
                        "• Área que ocupaba: %.2f m²\n\n" +
                        "Este producto ha sido removido del espacio asignado.\n\n" +
                        "Si este retiro no fue realizado por ti o necesitas asistencia, por favor contáctanos de inmediato.\n\n" +
                        "Gracias por confiar en Store-It.\n\n" +
                        "Saludos cordiales,\n" +
                        "El equipo de Store-It",
                cliente.getNombre(),
                producto.getNombre(),
                producto.getAreaOcupada()
                        .to(tech.units.indriya.unit.Units.SQUARE_METRE)
                        .getValue()
                        .doubleValue());

        // Enviamos el correo electrónico / Send email
        enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);
    }

    @Override
    public List<ProductoDto> listarProductosCliente(String email) throws ElementoNoEncontradoException {

        Cliente cliente = clienteRepo.findByUser_Email(email)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtener todos los productos relacionados con los espacios asignados al cliente
        List<Producto> productos = cliente.getProductos();

        // Mapear a DTO
        return productos.stream()
                .filter(producto -> producto.getEstadoProducto().equals(EstadoProducto.EN_BODEGA))
                .map(productoMapper::toDto)
                .toList();
    }


    @Override
    public List<ProductoDto> listarProductosClienteId(Long id) throws ElementoNoEncontradoException {

        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtener todos los productos relacionados con los espacios asignados al cliente
        List<Producto> productos = cliente.getProductos();

        // Mapear a DTO
        return productos.stream()
                .filter(producto -> producto.getEstadoProducto().equals(EstadoProducto.EN_BODEGA))
                .map(productoMapper::toDto)
                .toList();
    }


}
