package com.eturn.telegram.service;

import com.eturn.telegram.entity.Turn;

public interface TurnService {
    String createTurn(Turn turn, Long userId);
}
