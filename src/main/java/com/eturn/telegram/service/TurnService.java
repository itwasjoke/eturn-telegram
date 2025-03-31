package com.eturn.telegram.service;

import com.eturn.telegram.entity.Turn;

public interface TurnService {
    void startCreating(String name, Long userId);
    Turn finishCreating(String desc, Long userId);
}
