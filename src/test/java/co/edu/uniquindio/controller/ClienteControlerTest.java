package co.edu.uniquindio.controller;

import co.edu.uniquindio.StoreItApplication;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StoreItApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // 🔹 Deshabilita los filtros de seguridad en el test
@Import(ObjectMapper.class)  // 👈 Agregar esto
public class ClienteControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void actualizarTest() throws Exception {

        EditarClienteDTO cuentaDTO = new EditarClienteDTO(
                "1001330212",
                "Nicolas Cabrera"
        );

        // Simulación de la cookie de sesión con el nombre SESSIONID
        MockHttpServletRequestBuilder request = put("/api/clientes")
                .cookie(new Cookie("SESSIONID", "valor_de_sesion")) // Aquí debes colocar el valor real de la cookie
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cuentaDTO));

        // Realiza la petición y valída que el estado dé la respuesta sea OK
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void eliminarTest() throws Exception {

        // Simulación de la cookie de sesión con el nombre SESSIONID
        MockHttpServletRequestBuilder request = delete("/api/clientes/{id}", "102")
                .cookie(new Cookie("SESSIONID", "valor_de_sesion")) // Aquí debes colocar el valor real de la cookie
                .contentType("application/json");

        // Realiza la petición y valída que el estado dé la respuesta sea OK
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


    @Test
    public void obtenerTest() throws Exception {

        // Simulación de la cookie de sesión con el nombre SESSIONID
        MockHttpServletRequestBuilder request = get("/api/clientes/{id}", "1001330212")
                .cookie(new Cookie("SESSIONID", "valor_de_sesion")) // Reemplaza "valor_de_sesion" con un valor válido
                .contentType("application/json");

        // Realiza la petición y valída que el estado dé la respuesta sea OK
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


}
