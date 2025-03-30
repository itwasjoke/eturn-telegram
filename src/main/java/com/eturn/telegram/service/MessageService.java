package com.eturn.telegram.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface MessageService {
    InlineKeyboardMarkup createInlineKeyboard(
            List<String[]> buttonTextsAndCallbacks,
            int buttonsPerRow
    );
}
