package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.TurnService;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Service
public class ShowTurnListBtn implements ButtonAction {
    private final TurnService turnService;
    private final MessageService messageService;

    public ShowTurnListBtn(TurnService turnService, MessageService messageService) {
        this.turnService = turnService;
        this.messageService = messageService;
    }

    @Override
    public void handle(Update update, SilentSender sender) {
        List<Turn> turns = turnService.findTurns(update.getCallbackQuery().getFrom().getId());
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setParseMode("Markdown");
        message.setReplyMarkup(createKeyboard());

        if (turns.isEmpty()) {
            message.setText("Очередей нет. Создайте новую или вступите в существующую");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Ваши очереди:\n\n");
            for (Turn turn : turns) {
                sb.append(turn.getName());
                sb.append(" / `");
                sb.append(turn.getHash());
                sb.append("`\n");
            }
            message.setText(sb.toString());
        }
        sender.execute(message);
    }
    private InlineKeyboardMarkup createKeyboard() {
        List<String[]> btns = List.of(
                new String[]{"Перейти к очереди", "go_turn"},
                new String[]{"Вернуться", "go_main"}
        );
        return messageService.createInlineKeyboard(btns, 1);
    }
}
