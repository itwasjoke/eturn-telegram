package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ShowTurnsBtn implements ButtonAction {
    @Override
    public void handle(Update update, SilentSender sender) {

    }
}
