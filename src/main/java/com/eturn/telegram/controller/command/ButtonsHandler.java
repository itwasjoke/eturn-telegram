package com.eturn.telegram.controller.command;

import com.eturn.telegram.controller.command.btns.CreateTurnBtn;
import com.eturn.telegram.controller.command.btns.ShowTurnsBtn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ButtonsHandler {
    private final CreateTurnBtn createTurnBtn;
    private final Map<String, ButtonAction> actions = new HashMap<>();
    public ButtonsHandler(CreateTurnBtn createTurnBtn, ShowTurnsBtn showTurnsBtn) {
        this.createTurnBtn = createTurnBtn;
        actions.put("show_turns", showTurnsBtn);
    }

    public void handleAction(Update update, SilentSender sender) {
        String callbackData = update.getCallbackQuery().getData();
        ButtonAction buttonAction = actions.get(callbackData);
        if (buttonAction == null) {
            return;
        }
        buttonAction.handle(update, sender);
    }

    public ReplyFlow handleCreation(SilentSender sender) {
        log.info("Creating new button");
        return createTurnBtn.handle(sender);
    }
}
