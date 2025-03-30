package com.eturn.telegram.controller.command.btns;

import com.eturn.telegram.controller.command.ButtonAction;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.service.TurnService;
import com.eturn.telegram.service.impl.HashGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j2
public class CreateTurnBtn {

    private final TurnService turnService;

    public CreateTurnBtn(TurnService turnService) {
        this.turnService = turnService;
    }

    public ReplyFlow handle(SilentSender sender) {
        Turn turn = new Turn();
    
        return ReplyFlow.builder((DBContext) sender) // Исправлено
                .action((bot, upd) -> requestTurnName(sender, upd))
                .next(Reply.of((bot, upd) -> processTurnName(sender, upd, turn)))
                .next(Reply.of((bot, upd) -> processTurnDescription(sender, upd, turn)))
                .build();
    }
    
    private void requestTurnName(SilentSender sender, Update upd) {
        log.info("Step 1: Requesting turn name");
        sender.send("Введите название очереди", upd.getMessage().getChatId());
    }
    
    private void processTurnName(SilentSender sender, Update upd, Turn turn) {
        log.info("Step 2: Receiving turn name");
        if (upd.hasMessage() && upd.getMessage().hasText()) {
            turn.setName(upd.getMessage().getText());
            sender.send("Введите описание очереди", upd.getMessage().getChatId());
        } else {
            sender.send("Некорректный ввод. Пожалуйста, введите название очереди.", upd.getMessage().getChatId());
            log.warn("Invalid input for turn name: {}", upd);
        }
    }
    
    private void processTurnDescription(SilentSender sender, Update upd, Turn turn) {
        log.info("Step 3: Receiving turn description");
        if (upd.hasMessage() && upd.getMessage().hasText()) {
            turn.setDescription(upd.getMessage().getText());
            try {
                String hash = turnService.createTurn(turn, upd.getMessage().getFrom().getId());
                sendTurnDetails(sender, upd, turn, hash);
            } catch (Exception e) {
                log.error("Error creating turn", e);
                sender.send("Произошла ошибка при создании очереди.", upd.getMessage().getChatId());
            }
        } else {
            sender.send("Некорректный ввод. Пожалуйста, введите описание очереди.", upd.getMessage().getChatId());
            log.warn("Invalid input for turn description: {}", upd);
        }
    }
    
    private void sendTurnDetails(SilentSender sender, Update upd, Turn turn, String hash) {
        SendMessage msg = new SendMessage();
        msg.setChatId(upd.getMessage().getChatId());
        msg.setText(turn.getName() + " / " + hash + "\nОписание: " + turn.getDescription());
        sender.execute(msg);
        log.info("Step 4: Sending turn details");
    }
}
