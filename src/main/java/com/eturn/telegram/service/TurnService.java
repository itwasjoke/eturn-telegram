package com.eturn.telegram.service;

import com.eturn.telegram.dto.TurnWithPositionsDTO;
import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Turn;

import java.util.List;
import java.util.Optional;

public interface TurnService {
    void startCreating(String name, Long userId);
    Turn finishCreating(String desc, Long userId);
    Optional<Turn> getTurn(String id);
    Optional<TurnWithPositionsDTO> getTurnWithPositions(String id, Long userId);
    List<Turn> findTurns(Long userId);
    boolean turnExists(LocalUser user);
}
