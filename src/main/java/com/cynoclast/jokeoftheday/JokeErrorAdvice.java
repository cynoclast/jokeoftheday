package com.cynoclast.jokeoftheday;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class JokeErrorAdvice {

    /**
     * Handles 404
     * @param ex Joke not found
     * @return a 404 saying which id wasn't found
     */
    @ResponseBody
    @ExceptionHandler(JokeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String jokeNotFoundHandler(JokeNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * Treats ConstraintViolationException as trying to save two jokes to one day.
     * @param ex unique constraint violation
     * @return a 404
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)  // only constraint currently is unique day
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String oneJokePerDayHandler(ConstraintViolationException ex) {
        return ex.getMessage();
    }
}
