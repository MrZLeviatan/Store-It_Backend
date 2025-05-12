package co.edu.uniquindio.service.utils;


import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * {@code interface} que se implementara en {@link co.edu.uniquindio.service.utils.impl.CloudinaryServicioImpl} el cual define los métodos necesarios para interactuar con Cloudinary.
 * Permite subir imágenes a {@code Cloudinary} y eliminarlas usando el ID de la imagen.
 * La implementación de esta interfaz debe proporcionar la lógica para manejar las operaciones con la API de Cloudinary.
 *
 * @author MrZ. Leviatan
 */
public interface CloudinaryServicio {

    /**
     * Sube una imagen a Cloudinary y retorna la URL segura de la imagen subida.
     * La imagen se almacena en un directorio específico en Cloudinary.
     *
     * @param file El archivo de imagen que se desea subir.
     * @return La URL segura de la imagen subida a Cloudinary.
     */
    String uploadImage(MultipartFile file);


    /**
     * Elimina una imagen de Cloudinary utilizando su ID.
     * Este método elimina la imagen almacenada en la plataforma Cloudinary.
     *
     * @param idImagen El ID de la imagen que se desea eliminar.
     * @return Un mapa con el resultado de la operación de eliminación.
     * @throws Exception Si ocurre un error durante la eliminación de la imagen.
     */
    Map eliminarImagen(String idImagen);

}
