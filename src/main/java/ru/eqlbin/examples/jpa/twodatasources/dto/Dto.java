package ru.eqlbin.examples.jpa.twodatasources.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Dto {
    private Long id;
    private String value;

    public Dto(String value) {
        this.value = value;
    }
}
