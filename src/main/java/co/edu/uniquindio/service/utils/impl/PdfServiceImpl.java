package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.CargaFallidaException;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.service.utils.PdfService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * Implementación del servicio {@link PdfService} que genera archivos PDF a partir de plantillas HTML usando Thymeleaf.
 *
 * @author MrZ.Leviatan
 */
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    // Motor de plantillas Thymeleaf utilizado para renderizar el HTML
    private final TemplateEngine templateEngine;

    // Porcentaje de penalización (19%)
    private final BigDecimal penalizacion = new BigDecimal("0.19");


    /**
     * Genera un archivo PDF que representa un contrato, usando una plantilla HTML predefinida.
     *
     * @param contrato Objeto que contiene toda la información del contrato a mostrar en el PDF.
     * @return Un arreglo de bytes que representa el PDF generado.
     */
    @Override
    public byte[] generarContratoPdf(Contrato contrato) {

        // Calcular duración del contrato en meses
        long duracionMeses = ChronoUnit.MONTHS.between(
                contrato.getFechaInicio().withDayOfMonth(1), // normaliza al inicio del mes
                contrato.getFechaFin().withDayOfMonth(1)     // normaliza al inicio del mes
        );

        // Crear contexto de Thymeleaf con las variables necesarias para el HTML
        Context context = new Context();
        context.setVariable("arrendador", contrato.getAgenteVentas()); // Persona que arrienda el espacio (vendedor)
        context.setVariable("arrendatario", contrato.getCliente());    // Cliente que arrienda
        context.setVariable("espacio", contrato.getEspacio());         // Espacio alquilado
        context.setVariable("contrato", contrato);                     // Objeto contrato completo
        context.setVariable("duracion", duracionMeses);                // Duración del contrato
        context.setVariable("diaPago", contrato.getFechaInicio()
                .plusMonths(1)
                        .withDayOfMonth(Math.min(contrato.getFechaInicio().getDayOfMonth(),
                                contrato.getFechaInicio().plusMonths(1).lengthOfMonth()))); // Día de pago mensual
        context.setVariable("penalizacion", contrato.getValor().multiply(penalizacion)); // Monto penalización

        if (contrato.getFirmaCliente() != null) {
            // ITextRender no puede renderizar images binarias pero si Base64
            String firmaBase64 = Base64.getEncoder().encodeToString(contrato.getFirmaCliente());
            // Se crea una URL de imagen embebida en formato Base64
            context.setVariable("firmaCliente", "data:image/png;base64," + firmaBase64);
        } else {
            context.setVariable("firmaCliente", null);
        }

        if (contrato.getFirmaAgenteVentas() != null) {
            String firmaBase64 = Base64.getEncoder().encodeToString(contrato.getFirmaAgenteVentas());
            context.setVariable("firmaAgente", "data:image/png;base64," + firmaBase64);
        }else{
            context.setVariable("firmaAgente", null);
        }

        // Procesar el HTML con los datos inyectados
        String html = templateEngine.process("contrato", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Crear el renderizado y generar el PDF desde el HTML
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html); // Cargar el HTML renderizado
            renderer.layout();                    // Aplicar estilos y disposición
            renderer.createPDF(outputStream);     // Generar PDF
            return outputStream.toByteArray();    // Devolver los bytes del PDF
        } catch (IOException | com.lowagie.text.DocumentException e) {
            // Manejo de errores al generar el PDF
            throw new CargaFallidaException(MensajeError.ERROR_CONSTRUIR_PDF, e);
        }}


    /**
     * Genera un archivo PDF con el aviso de deuda de un contrato cancelado.
     *
     * <p>Este método utiliza una plantilla HTML llamada <b>"aviso-deuda"</b> que es procesada con Thymeleaf,
     * y luego convertida en PDF mediante la librería <b>Flying Saucer (iTextRenderer)</b>.</p>
     *
     * <p>La fecha de pago que se genera en el documento corresponde al último día del mes
     * dos meses después de la fecha actual.</p>
     *
     * @param contrato el contrato cancelado del cual se genera el aviso de deuda.
     * @return un arreglo de bytes que representa el archivo PDF generado.
     * @throws CargaFallidaException si ocurre un error durante el proceso de generación del PDF.
     */
    @Override
    public byte[] generarAvisoDeudaPdf(Contrato contrato) {

        // Crear el contexto para Thymeleaf y establecer las variables necesarias
        Context context = new Context();
        context.setVariable("contratoCancelado", contrato);  // Objeto contrato completo
        context.setVariable("fechaActual", LocalDate.now());
        context.setVariable("montoTotal",contrato.getValor().multiply(penalizacion));
        context.setVariable("fechaPago",LocalDate.now().plusMonths(2)
                .withDayOfMonth(LocalDate.now()
                        .plusMonths(2).lengthOfMonth())); // // Fecha de pago: 2 meses después de hoy, último día del mes

        // Procesar el HTML con los datos inyectados
        String html = templateEngine.process("aviso-deuda", context);

        // Crear el PDF a partir del HTML generado
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Crear el renderizado y generar el PDF desde el HTML
            ITextRenderer renderer = new ITextRenderer();  // Inicializar el renderizado
            renderer.setDocumentFromString(html);          // Cargar el HTML renderizado
            renderer.layout();                             // Aplicar estilos y disposición
            renderer.createPDF(outputStream);              // Generar PDF
            return outputStream.toByteArray();             // Devolver los bytes del PDF
        } catch (IOException | com.lowagie.text.DocumentException e) {
            // Manejo de errores al generar el PDF
            throw new CargaFallidaException(MensajeError.ERROR_CONSTRUIR_PDF, e);
        }}

}
