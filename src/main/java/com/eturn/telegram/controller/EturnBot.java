package com.eturn.telegram.controller;

import com.eturn.telegram.controller.command.ButtonsHandler;
import com.eturn.telegram.controller.command.btns.CreateTurnBtn;
import com.eturn.telegram.controller.command.btns.MainBtn;
import com.eturn.telegram.controller.command.btns.ShowTurnsBtn;
import com.eturn.telegram.enums.ActionEturn;
import com.eturn.telegram.service.ChatActionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component("eturnBot")
@Log4j2
public class EturnBot extends AbilityBot {

    private final MainBtn mainBtn;
    private final ButtonsHandler buttonsHandler;
    private final ChatActionService chatActionService;
    private final CreateTurnBtn createTurnBtn;
    private final ShowTurnsBtn showTurnsBtn;

    protected EturnBot(
            @Value("${token}") String botToken,
            @Value("${bot.name}") String botUsername,
            MainBtn mainBtn,
            ButtonsHandler buttonsHandler,
            ChatActionService chatActionService,
            CreateTurnBtn createTurnCommand,
            ShowTurnsBtn showTurnsBtn) {
        super(botToken, botUsername);
        this.mainBtn = mainBtn;
        this.buttonsHandler = buttonsHandler;
        this.chatActionService = chatActionService;
        this.createTurnBtn = createTurnCommand;
        this.showTurnsBtn = showTurnsBtn;
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    public Ability startBot(){
        return Ability
                .builder()
                .name("start")
                .info("Запустить Eturn")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> mainBtn.handle(ctx.update(), this.silent))
                .build();
    }
    public Ability stopBot(){
        return Ability
                .builder()
                .name("reset")
                .info("Перезапустить Eturn")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> mainBtn.handle(ctx.update(), this.silent))
                .build();
    }

    public Reply handleCallbackQueryReply() {
        return Reply.of(
                (bot, update) -> buttonsHandler.handleAction(update, bot.silent()),
                Flag.CALLBACK_QUERY,
                Update::hasCallbackQuery
        );
    }

    public Reply handleCreationDescReply() {
        return Reply.of(
                (bot, update) -> {
                    Long id = update.getMessage().getChatId();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(id);
                    ActionEturn actionEturn = chatActionService.getActionsEturn(id);
                    switch (actionEturn){
                        case CREATE_TURN_NAME:
                            createTurnBtn.startCreating(
                                    id,
                                    update.getMessage().getText()
                            );
                            sendMessage.setText("Введите описание очереди");
                            chatActionService.addAction(
                                    id,
                                    ActionEturn.CREATE_TURN_DESCRIPTION
                            );
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case CREATE_TURN_DESCRIPTION:
                            createTurnBtn.finishCreating(
                                    id,
                                    update.getMessage().getText(),
                                    silent
                            );
                            chatActionService.clearAction(id);
                            break;
                        case SHOW_TURN:
                            showTurnsBtn.showTurn(update, silent);
                            break;
                        default:
                            mainBtn.handle(update, bot.silent());
                    }
                },
                Flag.MESSAGE,
                upd -> !upd.getMessage().getText().startsWith("/")
        );
    }
}
