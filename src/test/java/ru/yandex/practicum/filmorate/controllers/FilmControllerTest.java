package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void whenCreateFilm_status200() {
        Film film = new Film(1, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();


        assertEquals(200, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateEmptyModel_Status400() {
        Film film = new Film(0, "", "", LocalDate.of(11,1,1), 1);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateFilm_BadDescription_status400() {

        String description = "";
        for (int i = 0; i < 210; i++) {
            description = description + "d";
        }

        Film film = new Film(1, "Drama", description ,
                LocalDate.of(2020, 12, 20), 90);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateCorrectlyDate_Status200() {
        Film film = new Film(1, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();


        assertEquals(200, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateUnCorrectlyDate_Status400() {
        Film film = new Film(1, "Drama", "Very big",
                LocalDate.of(1860, 12, 20), 90);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateUncorrectlyDuration_status400() {
        Film film = new Film(1, "Drama", "Very big",
                LocalDate.of(1860, 12, 20), -1);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenCreateBlankName_status400() {
        Film film = new Film(1, "", "Very big",
                LocalDate.of(1860, 12, 20), -1);

        MockHttpServletResponse response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();


        assertEquals(400, response.getStatus());
    }

    @SneakyThrows
    @Test
    void whenUpdateUnknownFilm_status404() {
        Film film = new Film(1, "Film", "Very big",
                LocalDate.of(1860, 12, 20), -1);

        MockHttpServletResponse response = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();


        assertEquals(404, response.getStatus());
    }
}