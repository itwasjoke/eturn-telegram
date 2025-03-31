package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import com.eturn.telegram.dto.PositionDTO;
import com.eturn.telegram.dto.TurnWithPositionsDTO;
import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.enums.ActionEturn;
import com.eturn.telegram.service.ChatActionService;
import com.eturn.telegram.service.MessageService;
import com.eturn.telegram.service.TurnService;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Component
public class ShowTurnsBtn implements ButtonAction {

    private final ChatActionService chatActionService;
    private final MessageService messageService;
    private final TurnService turnService;

    public ShowTurnsBtn(
            ChatActionService chatActionService,
            MessageService messageService, TurnService turnService
    ) {
        this.chatActionService = chatActionService;
        this.messageService = messageService;
        this.turnService = turnService;
    }

    @Override
    public void handle(Update update, SilentSender sender) {
        SendMessage message = new SendMessage();
        Long id = update.getCallbackQuery().getMessage().getChatId();
        message.setChatId(id);
        chatActionService.addAction(id, ActionEturn.SHOW_TURN);
        message.setText("Введи код очереди из 6 символов:");
        sender.execute(message);
    }

    public void showTurn(Update update, SilentSender sender) {
        Long id = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        String hash = update.getMessage().getText();
        Optional<TurnWithPositionsDTO> pos = turnService.getTurnWithPositions(hash, userId);

        SendMessage message = new SendMessage();
        message.setChatId(id);
        if (pos.isPresent()) {
            TurnWithPositionsDTO turn = pos.get();

            StringBuilder posFirst = new StringBuilder();
            StringBuilder posCurrent = new StringBuilder();
            StringBuilder suggestedForPosition = new StringBuilder();

            if (turn.current().isPresent()){
                PositionDTO position = turn.current().get();
                posCurrent.append("\n *#");
                posCurrent.append(position.count());
                posCurrent.append(" ");
                posCurrent.append(position.name());
                posCurrent.append("*\nПеред тобой *n* человек\n");
            }
            if (turn.first().isPresent()){
                PositionDTO position = turn.first().get();
                posFirst.append("\n *#");
                posFirst.append(position.count());
                posFirst.append(" ");
                posFirst.append(position.name());
                posFirst.append("*\nПозиция пропадет через *00:23*\n");
            }
            if (turn.first().isEmpty() && turn.current().isEmpty()){
                suggestedForPosition.append("\nВстаньте первым в очередь!");
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = formatter.format(turn.turn().getDateStart());
            message.setParseMode("Markdown");
            message.setReplyMarkup(createKeyboard());
            message.setText("*"+turn.turn().getName()
                    +"* / `"+turn.turn().getHash()
                    +"`\nОписание: "
                    +turn.turn().getDescription()
                    +"\n\nБудет действительна до "
                    +formattedDate
                    +"\n"
                    + posFirst
                    + posCurrent
                    + suggestedForPosition
            );
            message.setReplyMarkup(createKeyboard());
        } else {
            message.setText("Очередь не найдена");
        }
        sender.execute(message);
    }

    public void showTurnFounded(
            Long id,
            Optional<Turn> turnOptional,
            SilentSender sender
    ) {
        SendMessage message = new SendMessage();
        message.setChatId(id);
        if (turnOptional.isPresent()) {
            Turn turn = turnOptional.get();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = formatter.format(turn.getDateStart());
            message.setParseMode("Markdown");
            message.setReplyMarkup(createKeyboard());
            message.setText("*"+turn.getName()
                    +"* / `"+turn.getHash()
                    +"`\nОписание: "
                    +turn.getDescription()
                    +"\n\nБудет действительна до "
                    +formattedDate
                    +"\n\nЧтобы другие пользователи могли встать в очередь, отправьте им код `"
                    +turn.getHash()
                    +"`"
            );
        } else {
            message.setText("Очередь не найдена");
        }
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
