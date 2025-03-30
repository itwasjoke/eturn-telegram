package com.eturn.telegram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class LocalUser {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;

    @OneToMany(mappedBy = "localUser")
    private List<Position> positionList = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Turn> turns = new ArrayList<>();
}
