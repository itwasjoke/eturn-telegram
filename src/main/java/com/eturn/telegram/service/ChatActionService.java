package com.eturn.telegram.service;

import com.eturn.telegram.enums.ActionEturn;

public interface ChatActionService {
    ActionEturn getActionsEturn(Long chatId);
    void clearAction(Long chatId);
    void addAction(Long chatId, ActionEturn action);
}
