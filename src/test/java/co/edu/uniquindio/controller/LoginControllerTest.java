package co.edu.uniquindio.controller;

import co.edu.uniquindio.StoreItApplication;
import co.edu.uniquindio.dto.Login.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest(classes = StoreItApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginCorrectoClienteTest() throws Exception {
        // Simula datos de inicio de sesión válidos
        LoginDto loginDto = new LoginDto(
                "nikis281002@gmail.com",
                "65656565656");

        mockMvc.perform(post("/api/Autentifcar/iniciarSesion")
                        .contentType("application/json") // ✅ Usando el string directamente
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk()) // Espera código 200
                .andExpect( jsonPath("$.mensaje").value("CLIENTE"))
                .andExpect(jsonPath("$.error").value(false));
    }
}

