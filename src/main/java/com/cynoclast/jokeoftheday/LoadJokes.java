package com.cynoclast.jokeoftheday;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
class LoadJokes {
    private static final Logger log = LoggerFactory.getLogger(LoadJokes.class);

    @Bean
    CommandLineRunner init(JokeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Joke("What do you call a joke that isn’t funny? A sentence.", LocalDate.now())));
            log.info("Preloading " + repository.save(new Joke("Why do I drink coffee? I like to do stupid things faster and with more energy.", LocalDate.now().plusDays(1))));
            log.info("Preloading " + repository.save(new Joke("A conference call is the best way to get a dozen people to say bye 300 times.", LocalDate.now().plusDays(2), "Conference call bye!" )));
        };
    }
}
