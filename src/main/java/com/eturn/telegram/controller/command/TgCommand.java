package com.eturn.telegram.controller.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TgCommand {
    void getAnswer(Message message);
}
