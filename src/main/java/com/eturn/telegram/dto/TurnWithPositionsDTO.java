package com.eturn.telegram.dto;

import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;

import java.util.Optional;

public record TurnWithPositionsDTO(
        Turn turn,
        Optional<PositionDTO> first,
        Optional<PositionDTO> current
) {
}
