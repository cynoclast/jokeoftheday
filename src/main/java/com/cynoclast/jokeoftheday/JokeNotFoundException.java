package com.cynoclast.jokeoftheday;

class JokeNotFoundException extends RuntimeException {
    public JokeNotFoundException(Long id) {
        super("Could not find joke " + id);
    }
}
