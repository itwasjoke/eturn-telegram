package com.eturn.telegram.service;

import com.eturn.telegram.dto.PositionDTO;
import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;

import java.util.Optional;

public interface PositionService {
    Optional<PositionDTO> findFirstByUser(Turn turn, Long userId);
    Optional<PositionDTO> findCurrentByTurn(Turn turn, Long userId);
    boolean existsByUser(LocalUser user);
}
