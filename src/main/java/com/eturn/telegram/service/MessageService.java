package com.eturn.telegram.service;

import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface MessageService {
    InlineKeyboardMarkup createInlineKeyboard(
            List<String[]> buttonTextsAndCallbacks,
            int buttonsPerRow
    );
    void deleteMessage(long chatId, int messageId, SilentSender sender);
}
