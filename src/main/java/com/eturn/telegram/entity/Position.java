package com.eturn.telegram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Position {
    @Id
    private Long id;

    private Boolean started;

    @ManyToOne
    private LocalUser localUser;

    @ManyToOne
    private Turn turn;
}
