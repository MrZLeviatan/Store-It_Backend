package co.edu.uniquindio.service.service;

import co.edu.uniquindio.dto.PersonalBodegaDTO;

import java.util.List;

public interface PersonalBodegaService {

    /**
     * Crea una nueva cuenta del personal de bodega.
     *
     * @throws Exception Sí ocurre un error durante la creación.
     */
    void crearPersonalBodega(PersonalBodegaDTO cuentaPersonal) throws Exception;


    /**
     * Obtiene por su ID.
     *
     * @return PersonalDto con la información del personal.
     * @throws Exception Si el personal de bodega a buscar  no existe.
     */

    PersonalBodegaDTO obtenerPersonalBodegaPoId(String id) throws Exception;

    /**
     * Actualiza una cuenta existente.
     * @throws Exception Si la actualización falla o  no existe.
     */
    void actualizarPersonalBodega(PersonalBodegaDTO cuentaPersonal) throws Exception;

    /**
     * Elimina un personal de bodega por su ID.
     *
     * @throws Exception Si el personal de bodega no existe o no puede ser eliminado.
     */
    void eliminarPersonalBodega(String id) throws Exception;
    /**
     * Obtiene una lista paginada de todo el personal de bodega filtrados por nombre.
     *
     * @param pagina Número de página para la paginación.
     * @return Lista de PersonalBodegaDto con los detalles de las personas.
     */
    List<PersonalBodegaDTO> listarPersonalBod(int pagina);
}
