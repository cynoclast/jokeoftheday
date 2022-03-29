package com.cynoclast.jokeoftheday;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * A controller for managing jokes of the day, with CRUD and get all operations.
 */
@RestController
@RequestMapping("api/jokes")
public class JokeController {
    private final JokeRepository repository;

    /**
     * Creates this controller.
     *
     * @param repository the joke repo
     */
    JokeController(JokeRepository repository) {
        this.repository = repository;
    }

    /**
     * Gets the joke of the day.
     *
     * @return the joke of the day
     */
    @GetMapping("/today")
    Joke getTheJokeOfTheDay() {
        LocalDate today = LocalDate.now();
        return repository.findByDate(today);
    }

    /**
     * The joke for a given date.
     *
     * @param date the date to get a joke for
     * @return the joke for that date
     */
    @GetMapping("/dates")
    Joke findByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repository.findByDate(date);
    }

    /**
     * Gets all jokes.
     *
     * @return all saved jokes
     */
    @GetMapping
    List<Joke> all() {
        return repository.findAll();
    }

    /**
     * Posts a new joke
     *
     * @param newJoke the new joke to save
     * @return the created joke
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    Joke postNewJoke(@RequestBody Joke newJoke) {
        return repository.save(newJoke);
    }

    /**
     * Gets a particular joke by id.
     *
     * @param id the id of the joke to get
     * @return the joke with that id
     */
    @GetMapping("/{id}")
    Joke getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new JokeNotFoundException(id));
    }

    /**
     * Replaces a joke by id
     *
     * @param newJoke the new joke
     * @param id      the id of the joke to replace
     * @return the updated joke
     */
    @PutMapping("/{id}")
    Joke replaceJokeById(@RequestBody Joke newJoke, @PathVariable Long id) {
        return repository.findById(id)
                .map(joke -> {
                    joke.setJoke(newJoke.getJoke());
                    joke.setDate(newJoke.getDate());
                    joke.setDescription(newJoke.getDescription());
                    return repository.save(joke);
                }).orElseGet(() -> {
                    newJoke.setId(id);
                    return repository.save(newJoke);
                });
    }

    /**
     * Deletes a joke.
     *
     * @param id the id of the joke to delete
     */
    @DeleteMapping("/{id}")
    void deleteJokeById(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
