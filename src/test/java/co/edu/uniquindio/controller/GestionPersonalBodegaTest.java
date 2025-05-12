package co.edu.uniquindio.controller;

import co.edu.uniquindio.StoreItApplication;
import co.edu.uniquindio.dto.users.personalBodega.CrearPersonalBodegaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StoreItApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // 🔹 Deshabilita filtros de seguridad para pruebas
@Import(ObjectMapper.class)
public class GestionPersonalBodegaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void crearPersonalBodegaCorrectoTest() throws Exception {

        // 🔹 Crear un DTO con los datos válidos
        CrearPersonalBodegaDto dto = new CrearPersonalBodegaDto(
                "PB101",
                "Carlos",
                "Ramírez",
                "carlos.ramirez@example.com",
                "3001234567",
                "Supervisor"
                // No se incluye fechaIngreso, debe ser asignada automáticamente en el mapper
        );

        // 🔹 Enviar solicitud POST y esperar código 201 CREATED
        mockMvc.perform(post("/api/personal-bodega")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }


    @Test
    public void obtenerTest() throws Exception {

        MockHttpServletRequestBuilder request = get("/api/personal-bodega/{id}", "PB101")
                .cookie(new Cookie("SESSIONID", "valor_de_sesion"))
                .contentType("application/json");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }



}

