package com.cynoclast.jokeoftheday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JokeOfTheDayCrudTest {

    /**
     * Object mapper that handles JDK 8 and Java time stuff.
     */
    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Gets the joke for today")
    public void getTheJokeForToday() throws Exception {
        LocalDate today = LocalDate.now();

        this.mockMvc.perform(get("/api/jokes/today")
                        .queryParam("date", today.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("What do you call a joke that isn’t funny? A sentence."));
    }

    @Test
    @DisplayName("Happy path CRUD test with no collisions on preloaded jokes")
    public void crud() throws Exception {

        LocalDate somewhatInTheFuture = LocalDate.now().plusYears(42);

        // create a joke
        this.mockMvc.perform(post("/api/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Joke("joke4", somewhatInTheFuture))))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("joke4"));

        // get our joke
        this.mockMvc.perform(get("/api/jokes/4")  // <----------this is assuming we preload 3 jokes
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("joke4"));

        // change (put) our joke
        this.mockMvc.perform(put("/api/jokes/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Joke("joke4.2", somewhatInTheFuture))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("joke4.2"));

        // delete our joke
        this.mockMvc.perform(delete("/api/jokes/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // find all preloaded jokes
        this.mockMvc.perform(get("/api/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].joke").value("What do you call a joke that isn’t funny? A sentence."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].joke").value("Why do I drink coffee? I like to do stupid things faster and with more energy."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].joke").value("A conference call is the best way to get a dozen people to say bye 300 times."));
    }

    @Test
    @DisplayName("Joke id 9001 doesn't exist")
    public void notFound() throws Exception {
        this.mockMvc.perform(get("/api/jokes/9001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Could not find joke with id 9001")));
    }

    @Test
    @DisplayName("Can find the first preloaded joke which should be the current date")
    public void findByDate() throws Exception {
        LocalDate today = LocalDate.now();

        this.mockMvc.perform(get("/api/jokes/dates")
                        .queryParam("date", today.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("What do you call a joke that isn’t funny? A sentence."));
    }

    @Test
    @DisplayName("Can only have one joke per day")
    public void oneJokePerDay() throws Exception {

        LocalDate somewhatInTheFuture = LocalDate.now().plusYears(100);

        // create a joke
        this.mockMvc.perform(post("/api/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Joke("joke4", somewhatInTheFuture))))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.joke").value("joke4"));

        // try and create another for the same day
        this.mockMvc.perform(post("/api/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Joke("joke4", somewhatInTheFuture))))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Takes objects and makes JSON.
     * <p>
     * Supports fancy JDK 8 (formerly joda) dates.
     *
     * @param obj the object to JSONify.
     * @return JSON string representing the object
     */
    public static String asJsonString(final Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
