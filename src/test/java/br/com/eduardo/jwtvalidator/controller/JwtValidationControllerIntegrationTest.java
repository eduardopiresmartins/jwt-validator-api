package br.com.eduardo.jwtvalidator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class JwtValidationControllerIntegrationTest {

    private static final String ENDPOINT = "/api/v1/jwt/validate";

    private static final String TOKEN_CASO_1 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private static final String TOKEN_CASO_2 = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private static final String TOKEN_CASO_3 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    private static final String TOKEN_CASO_4 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarTrueParaOCaso1Oficial() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(bodyComToken(TOKEN_CASO_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void deveRetornarFalseParaOCaso2Oficial() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(bodyComToken(TOKEN_CASO_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void deveRetornarFalseParaOCaso3Oficial() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(bodyComToken(TOKEN_CASO_3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void deveRetornarFalseParaOCaso4Oficial() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(bodyComToken(TOKEN_CASO_4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void deveRetornarBadRequestQuandoTokenForVazio() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"token":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoTokenForAusente() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andExpect(status().isBadRequest());
    }

    private String bodyComToken(String token) {
        return """
                {"token":"%s"}
                """.formatted(token);
    }
}
