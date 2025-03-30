package com.eturn.telegram.controller.command;

import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ButtonAction {
    void handle(Update update, SilentSender sender);
}
