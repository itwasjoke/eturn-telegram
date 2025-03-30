package com.eturn.telegram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Turn {
    @Id
    private String hash;
    private String name;
    private String description;
    private Date dateStart;

    @ManyToOne
    private LocalUser creator;

    @OneToMany(mappedBy = "turn")
    private List<Position> positions = new ArrayList<>();

    // time

    private Double smoothedValue;

    private Long totalTime = 0L;

    private Integer averageTime = 0;

    private Integer countPositionsLeft = 0;
}
