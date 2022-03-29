package com.cynoclast.jokeoftheday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface JokeRepository extends JpaRepository<Joke, Long> {
    Joke findByDate(LocalDate date);
}
