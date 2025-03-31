package com.eturn.telegram.controller.command;

import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.TurnService;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CreateTurnCommand {
    private final TurnService turnService;
    private final MessageService messageService;

    public CreateTurnCommand(
            TurnService turnService,
            MessageService messageService
    ) {
        this.turnService = turnService;
        this.messageService = messageService;
    }

    public void startCreating(Long chatId, String name) {
        turnService.startCreating(name, chatId);
    }
    public void finishCreating(Long chatId, String desc, SilentSender sender) {
        Turn turn = turnService.finishCreating(desc, chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = formatter.format(turn.getDateStart());
        message.setParseMode("Markdown");
        message.setReplyMarkup(createKeyboard());
        message.setText("*"+turn.getName()+"* / "+turn.getHash()+"\nОписание: "+desc+"\n\nБудет действительна до "+formattedDate+"\n\nЧтобы другие пользователи могли встать в очередь, отправьте им код `"+turn.getHash()+"`");
        sender.execute(message);
    }

    private InlineKeyboardMarkup createKeyboard() {
        List<String[]> btns = List.of(
                new String[]{"Встать в очередь", "create_position"},
                new String[]{"Заявки", "show_members"},
                new String[]{"Вернуться", "go_main"}
        );
        return messageService.createInlineKeyboard(btns, 2);
    }
}
