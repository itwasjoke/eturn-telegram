package com.eturn.telegram.service.impl;

import com.eturn.telegram.service.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    /**
     * Создаёт Inline-кнопки под сообщением.
     * @param buttonTextsAndCallbacks Список пар: ["Текст кнопки", "callback_data"]
     * @param buttonsPerRow Количество кнопок в одном ряду
     */
    public InlineKeyboardMarkup createInlineKeyboard(
            List<String[]> buttonTextsAndCallbacks,
            int buttonsPerRow
    ) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (String[] buttonData : buttonTextsAndCallbacks) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonData[0]);
            button.setCallbackData(buttonData[1]);
            rowInline.add(button);

            if (rowInline.size() >= buttonsPerRow) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
        }

        if (!rowInline.isEmpty()) {
            rowsInline.add(rowInline);
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
