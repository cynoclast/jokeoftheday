package com.cynoclast.jokeoftheday;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * A joke.
 * id and the date are unique.
 * Date format is yyyy-MM-dd.
 */
@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Joke {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String joke;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(unique = true)
    private LocalDate date;

    private String description;

    public Joke(String joke, LocalDate date) {
        this.joke = joke;
        this.date = date;
    }

    public Joke(String joke, LocalDate date, String description) {
        this.joke = joke;
        this.date = date;
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("joke", joke)
                .append("date", date)
                .append("description", description)
                .build();
    }
}
