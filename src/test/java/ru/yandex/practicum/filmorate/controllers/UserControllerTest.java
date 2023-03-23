package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void whenNullValueLoginReturn400() {
        User incorrectUser = new User(1, "mailTest@yandex.ru", "", "Ter",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenId_is_9999() {
        MockHttpServletResponse response = mockMvc.perform(get("/users/9999")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        assertEquals(404, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenNullValueUser_EmptyModel_PostReturn400() {
        User incorrectUser = new User(0, "", "", "", LocalDate.of(1,1,1));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }
    @SneakyThrows
    @Test
    void whenUnknownUserPutReturn404() {

        User incorrectUserTwo = new User(500, "mailTest@yandex.ru", "", "Ter",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse responseTwo = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUserTwo)))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse();


        assertEquals(404, responseTwo.getStatus());
    }

    @SneakyThrows
    @Test
    void whenBadEmailUserReturn400() {

        User incorrectUser = new User(1, "maiakajdjnnanj@", "login", "Ter",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenNameUserEmptyReturn200_changeNameToLogin() {

        User incorrectUser = new User(1, "mailTest@yandex.ru", "Michail", "",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        User incorrectUser2 = new User(1, "mailTest@yandex.ru", "Michail", "Michail",
                LocalDate.of(2002, 10, 5));

        assertEquals(200, response.getStatus());
        assertEquals(objectMapper.writeValueAsString(incorrectUser2), response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void createCorrectlyUser() {
        ;
        User incorrectUser = new User(1, "mailTest@yandex.ru", "Michail", "name",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

    }

    @SneakyThrows
    @Test
    void whenBadEmailUser_EmptyEmail_Return400() {

        User incorrectUser = new User(1, "", "login", "Ter",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenBadDate_BeforeBirthDay_Return400() {

        User incorrectUser = new User(1, "mailtest@yandex.ru", "login", "Ter",
                LocalDate.of(2025, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenTryCreateEmailAlreadyRegistred() {
        User User = new User(1, "mailTest@yandex.ru", "Michail", "name",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(User)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        User incorrectUser = new User(2, "mailTest@yandex.ru", "Michail", "name",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse responseTwo = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertEquals(400, responseTwo.getStatus());
    }

    @SneakyThrows
    @Test
    void whenUpdateUserAlredyRigistredEmail(){
        User User = new User(1, "mailTest@yandex.ru", "Michail", "name",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(User)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        User incorrectUser = new User(1, "mailTest@yandex.ru", "MichailUpdate", "nameSecond",
                LocalDate.of(2002, 10, 5));
        MockHttpServletResponse responseTwo = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(incorrectUser)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        assertEquals(200, responseTwo.getStatus());
        }
    }