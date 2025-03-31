package com.eturn.telegram.controller.command;

import com.eturn.telegram.controller.command.btns.CreateTurnBtn;
import com.eturn.telegram.controller.command.btns.MainBtn;
import com.eturn.telegram.controller.command.btns.ShowTurnListBtn;
import com.eturn.telegram.controller.command.btns.ShowTurnsBtn;
import com.eturn.telegram.service.MessageService;
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
    private final MessageService messageService;

    public ButtonsHandler(
            ShowTurnsBtn showTurnsBtn,
            MainBtn mainBtn,
            CreateTurnBtn createTurnBtn,
            ShowTurnListBtn showTurnListBtn,
            MessageService messageService) {
        actions.put("go_turn", showTurnsBtn);
        actions.put("go_main", mainBtn);
        actions.put("create_turn", createTurnBtn);
        actions.put("show_turns", showTurnListBtn);
        this.messageService = messageService;
    }

    public void handleAction(Update update, SilentSender sender) {
        String callbackData = update.getCallbackQuery().getData();
        ButtonAction buttonAction = actions.get(callbackData);
        if (buttonAction == null) {
            return;
        }
        buttonAction.handle(update, sender);
        messageService.deleteMessage(
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId(),
                sender
        );
    }
}
