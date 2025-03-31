package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.TurnService;
import com.eturn.telegram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Service
@Log4j2
public class MainBtn implements ButtonAction {
    private final UserService userService;
    private final MessageService messageService;
    private final TurnService turnService;

    public MainBtn(
            UserService userService,
            MessageService messageService,
            TurnService turnService) {
        this.userService = userService;
        this.messageService = messageService;
        this.turnService = turnService;
    }

    @Override
    @Transactional
    public void handle(Update update, SilentSender sender) {
        log.info("Get answer");
        User user;
        Message message;
        if (update.hasCallbackQuery() && update.getMessage()==null) {
            user = update.getCallbackQuery().getFrom();
            message = update.getCallbackQuery().getMessage();
        } else {
            user = update.getMessage().getFrom();
            message = update.getMessage();
        }
        LocalUser localUser = userService.registerUser(user);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Добро пожаловать, "+localUser.getFirstName()+"! \n\n Это Eturn - сервис электронных очередей!");
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(getKeyboard(localUser));
        sender.execute(sendMessage);
    }

    private InlineKeyboardMarkup getKeyboard(LocalUser localUser) {
        List<String[]> btns;
        int rows;
        if (!turnService.turnExists(localUser)) {
            btns = List.of(
                    new String[]{"Перейти к очереди", "go_turn"},
                    new String[]{"Создать очередь", "create_turn"}
            );
            rows = 1;
        } else {
            btns = List.of(
                    new String[]{"Мои очереди", "show_turns"},
                    new String[]{"Перейти к очереди", "go_turn"},
                    new String[]{"Создать очередь", "create_turn"}
            );
            rows = 2;
        }
        return messageService.createInlineKeyboard(btns, rows);
    }
}
