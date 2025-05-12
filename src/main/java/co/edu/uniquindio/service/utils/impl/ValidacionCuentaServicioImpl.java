package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.base.Persona;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.repository.users.RecursosHumanosRepo;
import co.edu.uniquindio.service.utils.ValidacionCuentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementación del servicio de validación de datos únicos de cuentas.
 * <p>
 * Este servicio permite verificar que correos electrónicos (personales y empresariales)
 * y números de teléfono no estén registrados previamente en el sistema.
 * </p>
 * <p>
 * La verificación incluye las entidades: Cliente, Agente de Ventas y Personal de Bodega.
 */
@Service
@RequiredArgsConstructor
public class ValidacionCuentaServicioImpl implements ValidacionCuentaServicio {


    private final ClienteRepo clienteRepo;
    private final AgenteVentasRepo agenteRepo;
    private final PersonalBodegaRepo personalRepo;
    private final RecursosHumanosRepo recursosHumanosRepo;


    /**
     * Verifica si el email personal ya está registrado en alguna cuenta activa o eliminada.
     * <p>
     * Si el email está asociado a una cuenta eliminada, se lanza una excepción específica.
     * Si el email ya existe en una cuenta activa, se lanza una excepción de elemento repetido.
     * @param email Correo electrónico a validar.
     * @throws ElementoRepetidoException Si el email ya está registrado.
     * @throws ElementoEliminadoException Si el email pertenece a una cuenta eliminada.
     */
    @Override
    public void validarEmailNoRepetido(String email) throws ElementoRepetidoException, ElementoEliminadoException {
        // Se busca en el repositorio de cada tipo de usuario (Cliente, Agente de Ventas, Personal de Bodega, Recursos Humanos)
        // Si el email existe en alguno de los repositorios, se retorna la persona encontrada.
        Optional<? extends Persona> persona =
                clienteRepo.findByUser_Email(email)
                        .map(c -> (Persona) c)// // Sí se encuentra, casteamos el objeto como Persona.
                        .or(() -> agenteRepo.findByUser_Email(email).map(a -> (Persona) a))
                        .or(() -> personalRepo.findByUser_Email(email).map(p -> (Persona) p))
                        .or(() -> recursosHumanosRepo.findByUser_Email(email).map(r->(Persona)r));

        if (persona.isPresent()) {
            // Verifica si la cuenta no esta eliminada.
            if (persona.get().getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                throw new ElementoEliminadoException(MensajeError.EMAIL_CUENTA_ELIMINADA);}

            throw new ElementoRepetidoException(MensajeError.EMAIL_YA_EXISTE);
        }}


    /**
     * Verifica si el email empresarial ya está registrado en agentes, personal de bodega o recursos humanos.
     * <p>
     * Este correo debe ser único dentro de los datos laborales de los empleados./
     * @param emailEmpresarial Correo empresarial a validar.
     * @throws ElementoRepetidoException Si ya existe una cuenta con este email empresarial.
     */
    @Override
    public void validarEmailEmpresarialNoRepetido(String emailEmpresarial) throws ElementoRepetidoException  {
        Optional<? extends Persona> persona =
                agenteRepo.findByDatosLaborales_EmailEmpresarial(emailEmpresarial)
                        .map(c -> (Persona) c)// // Sí se encuentra, casteamos el objeto como Persona.
                        .or(() -> personalRepo.findByDatosLaborales_EmailEmpresarial(emailEmpresarial).map(p -> (Persona) p))
                        .or(() -> recursosHumanosRepo.findByDatosLaborales_EmailEmpresarial(emailEmpresarial).map(r->(Persona)r));

        if (persona.isPresent()) {
            if (persona.get().getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                throw new ElementoEliminadoException(MensajeError.EMAIL_CUENTA_ELIMINADA);
            }

            throw new ElementoRepetidoException(MensajeError.EMAIL_YA_EXISTE);

        }}


    /**
     * Verifica si el número de teléfono está registrado como principal o secundario
     * en clientes, agentes de ventas, personal de bodega o recursos humanos.
     * <p>
     * El número debe estar previamente formateado (por ejemplo, con libphonenumber).
     * @param telefono Número de teléfono a validar.
     * @throws ElementoRepetidoException Si el teléfono ya está registrado en cualquier entidad.
     */
    @Override
    public void validarTelefonoNoRepetido(String telefono) throws ElementoRepetidoException {

        // Si el teléfono es nulo rompe la lógica y se devuelve.
        if (telefono == null || telefono.isBlank()) {
            return;}

        // Checamos si el teléfono o teléfono secundario ya existe en cualquiera de las entidades
        boolean existe = Stream.of(
                clienteRepo.findByTelefono(telefono),
                clienteRepo.findByTelefonoSecundario(telefono),
                agenteRepo.findByTelefono(telefono),
                agenteRepo.findByTelefonoSecundario(telefono),
                personalRepo.findByTelefono(telefono),
                personalRepo.findByTelefonoSecundario(telefono),
                recursosHumanosRepo.findByTelefono(telefono),
                recursosHumanosRepo.findByTelefonoSecundario(telefono)
        ).anyMatch(Optional::isPresent); // Si alguno está presente, el teléfono ya existe

        if (existe) {
            throw new ElementoRepetidoException(MensajeError.TELEFONO_YA_EXISTENTE);}
    }

}
