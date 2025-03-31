package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.enums.ActionEturn;
import com.eturn.telegram.service.ChatActionService;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.TurnService;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTurnBtn implements ButtonAction {
    private final TurnService turnService;
    private final ChatActionService chatActionService;
    private final ShowTurnsBtn showTurnsBtn;
    public CreateTurnBtn(
            TurnService turnService,
            ChatActionService chatActionService,
            ShowTurnsBtn showTurnsBtn
    ) {
        this.turnService = turnService;
        this.chatActionService = chatActionService;
        this.showTurnsBtn = showTurnsBtn;
    }
    public void startCreating(Long chatId, String name) {
        turnService.startCreating(name, chatId);
    }
    public void finishCreating(
            Long chatId,
            String desc,
            SilentSender sender
    ) {
        Turn turn = turnService.finishCreating(desc, chatId);
        showTurnsBtn.showTurnFounded(chatId, Optional.of(turn), sender);
    }

    @Override
    public void handle(Update update, SilentSender sender) {
        SendMessage sendMessage = new SendMessage();
        Long id = update.getCallbackQuery().getMessage().getChatId();
        sendMessage.setChatId(id);
        sendMessage.setText("Введите название очереди");
        chatActionService.addAction(id, ActionEturn.CREATE_TURN_NAME);
        sender.execute(sendMessage);
    }
}
