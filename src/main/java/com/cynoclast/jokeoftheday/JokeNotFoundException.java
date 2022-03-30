package com.cynoclast.jokeoftheday;

class JokeNotFoundException extends RuntimeException {
    /**
     * Lets you know it couldn't find joke with <pre>id</pre>
     *
     * @param id the id that wasn't found
     */
    public JokeNotFoundException(Long id) {
        super("Could not find joke with id " + id);
    }
}
