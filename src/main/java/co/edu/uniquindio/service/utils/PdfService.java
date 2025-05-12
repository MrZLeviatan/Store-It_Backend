package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.model.objects.Contrato;

/**
 * Servicio para la generación de documentos PDF.
 * Esta interfaz define los métodos necesarios para generar PDF a partir de información de contratos.
 *
 * @author MrZ.Leviatan
 */
public interface PdfService {

    /**
     * Genera un archivo PDF que representa un contrato.
     *
     * @param contrato El contrato del cual se generará el PDF.
     * @return Un arreglo de bytes que contiene el archivo PDF generado.
     */
    byte[] generarContratoPdf(Contrato contrato);


    /**
     * Genera un archivo PDF con el aviso de deuda correspondiente a un contrato cancelado.
     *
     * @param contrato el contrato sobre el cual se genera el aviso de deuda.
     * @return un arreglo de bytes que representa el archivo PDF generado.
     */
    byte[] generarAvisoDeudaPdf(Contrato contrato);

}
