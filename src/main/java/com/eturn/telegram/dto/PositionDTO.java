package com.eturn.telegram.dto;

import java.util.Date;

public record PositionDTO(
        String name,
        Integer count,
        Date dateStart,
        Boolean started
) {
}
