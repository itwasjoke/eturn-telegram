package com.eturn.telegram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class LocalUser {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;

    @OneToMany(mappedBy = "localUser", fetch = FetchType.LAZY)
    private Set<Position> positionList = new HashSet<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Turn> turns = new HashSet<>();
}
