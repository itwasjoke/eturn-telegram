package com.eturn.telegram.controller.command;

import com.eturn.telegram.controller.command.btns.ShowTurnsBtn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ButtonsHandler {
    private final Map<String, ButtonAction> actions = new HashMap<>();
    public ButtonsHandler(
            ShowTurnsBtn showTurnsBtn,
            RegCommand regCommand
    ) {
        actions.put("show_turns", showTurnsBtn);
        actions.put("go_main", regCommand);
    }

    public void handleAction(Update update, SilentSender sender) {
        String callbackData = update.getCallbackQuery().getData();
        ButtonAction buttonAction = actions.get(callbackData);
        if (buttonAction == null) {
            return;
        }
        buttonAction.handle(update, sender);
    }
}
