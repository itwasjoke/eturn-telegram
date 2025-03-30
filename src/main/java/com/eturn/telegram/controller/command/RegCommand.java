package com.eturn.telegram.controller.command;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.UserService;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Service
@Log4j2
public class RegCommand {
    private final UserService userService;
    private final MessageService messageService;

    public RegCommand(
            UserService userService,
            MessageService messageService
    ) {
        this.userService = userService;
        this.messageService = messageService;
    }

    public void getAnswer(Message message, SilentSender silentSender) {
        log.info("Get answer");
        LocalUser localUser = userService.registerUser(message);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Добро пожаловать, "+localUser.getFirstName()+"! \n\n Это Eturn - сервис электронных очередей!");
        List<String[]> btns;
        InlineKeyboardMarkup inlineKeyboardMarkup;
        if (localUser.getTurns().isEmpty()){
            btns = List.of(
                    new String[]{"Перейти к очереди", "go_turn"},
                    new String[]{"Создать очередь", "create_turn"}
            );
            inlineKeyboardMarkup = messageService.createInlineKeyboard(btns, 1);
        } else {
            btns = List.of(
                    new String[]{"Мои очереди", "show_turns"},
                    new String[]{"Перейти к очереди", "go_turn"},
                    new String[]{"Создать очередь", "create_turn"}
            );
            inlineKeyboardMarkup = messageService.createInlineKeyboard(btns, 2);
        }

        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        silentSender.execute(sendMessage);
    }
}
