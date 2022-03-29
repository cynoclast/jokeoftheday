package com.cynoclast.jokeoftheday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface JokeRepository extends JpaRepository<Joke, Long> {
    /**
     * Finds a joke for the given date.
     *
     * @param date the date to find a joke for
     * @return the joke for that day
     */
    Joke findByDate(LocalDate date);
}
