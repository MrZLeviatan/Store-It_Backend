package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.CargaFallidaException;
import co.edu.uniquindio.service.utils.CloudinaryServicio;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Implementación de la clase {@link CloudinaryServicio} para superponer el {@code Service} en la integración con Cloudinary para la carga y gestión de imágenes.
 * Esta clase utiliza la librería {@code Cloudinary} para subir imágenes a la nube y gestionar su eliminación.
 * La configuración de Cloudinary se inyecta desde el archivo {@code application.yml}.
 *
 * @author MrZ.Leviatan
 */
@Service
public class CloudinaryServicioImpl implements CloudinaryServicio {


    private final Cloudinary cloudinary;

    /**
     * Constructor que configura la conexión con Cloudinary utilizando las credenciales proporcionadas
     * en el archivo application.yml.
     *
     * @param cloudName Nombre de la cuenta de Cloudinary.
     * @param apiKey Clave de API de Cloudinary.
     * @param apiSecret Secreto de la API de Cloudinary.
     */
    public CloudinaryServicioImpl(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        // Configura el cliente de Cloudinary con las credenciales proporcionadas
        this.cloudinary = new Cloudinary(config);
    }

    /**
     * Sube una imagen a Cloudinary y devuelve la URL segura de la imagen subida.
     * La imagen se guarda en la carpeta "clientes" de Cloudinary.
     *
     * @param file El archivo de imagen que se desea subir.
     * @return La URL segura de la imagen subida.
     * @throws CargaFallidaException Si ocurre un error al convertir o subir la imagen.
     */
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // Convertimos el archivo MultipartFile a File
            File archivo = convertir(file);

            // Subimos el archivo a Cloudinary utilizando la versión correcta de la API
            Map<String, Object> uploadResult = cloudinary.uploader().upload(archivo, ObjectUtils.asMap("folder", "usuarios"));

            // Extraemos la URL de la respuesta
            String url = (String) uploadResult.get("url");

            // Devolvemos la URL
            return url;
        } catch (IOException e) {
            // En caso de error, lanzamos una excepción personalizada
            throw new CargaFallidaException(MensajeError.ERROR_SUBIR_IMAGEN, e);
        }
    }


    private File convertir(MultipartFile imagen) throws IOException {
        // Creamos un archivo temporal para almacenar el contenido del MultipartFile
        File file = File.createTempFile(imagen.getOriginalFilename(), null);

        // Escribimos los bytes del MultipartFile en el archivo temporal
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imagen.getBytes());
        }

        return file;
    }

    /**
     * Elimina una imagen de Cloudinary utilizando su ID.
     *
     * @param idImagen El ID de la imagen que se desea eliminar.
     * @return Un mapa con el resultado de la operación de eliminación.
     * @throws Exception Si ocurre un error durante la eliminación de la imagen.
     */
    @Override
    public Map eliminarImagen(String idImagen){
        try {
            return cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
        }catch (Exception e) {
            throw new CargaFallidaException(MensajeError.ERROR_ELIMINAR_IMAGEN, e);
        }}

}
