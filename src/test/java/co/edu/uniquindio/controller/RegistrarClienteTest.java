package co.edu.uniquindio.controller;


import co.edu.uniquindio.StoreItApplication;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StoreItApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // 🔹 Deshabilita los filtros de seguridad en el test
@Import(ObjectMapper.class)  // 👈 Agregar esto
public class RegistrarClienteTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void crearCorrectoTest() throws Exception {

        //Se crea un objeto para realizar la creación de la cuenta
        CrearClienteDto cuentaDTO = new CrearClienteDto(
                "103",
                "Luis Perez",
                "william@example.com",
                "1234567890"
        );

        //Se realiza la petición POST al servidor usando el MockMvc y se valida que el estado de la respuesta sea 201
        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated()); // Espera que la respuesta tenga el código 201 (CREATED)

    }

}
