package com.eturn.telegram.controller;

import com.eturn.telegram.controller.command.ButtonsHandler;
import com.eturn.telegram.controller.command.CreateTurnCommand;
import com.eturn.telegram.controller.command.RegCommand;
import com.eturn.telegram.entity.ChatAction;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.enums.ActionEturn;
import com.eturn.telegram.service.ChatActionService;
import com.eturn.telegram.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component("eturnBot")
@Log4j2
public class EturnBot extends AbilityBot {

    private final RegCommand regCommand;
    private final ButtonsHandler buttonsHandler;
    private final ChatActionService chatActionService;
    private final CreateTurnCommand createTurnCommand;

    protected EturnBot(
            @Value("${token}") String botToken,
            @Value("${bot.name}") String botUsername,
            RegCommand regCommand,
            ButtonsHandler buttonsHandler,
            ChatActionService chatActionService,
            CreateTurnCommand createTurnCommand
    ) {
        super(botToken, botUsername);
        this.regCommand = regCommand;
        this.buttonsHandler = buttonsHandler;
        this.chatActionService = chatActionService;
        this.createTurnCommand = createTurnCommand;
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
                .action(ctx -> regCommand.handle(ctx.update(), this.silent))
                .build();
    }

    public Reply handleCallbackQueryReply() {
        return Reply.of(
                (bot, update) -> buttonsHandler.handleAction(update, bot.silent()),
                Flag.CALLBACK_QUERY,
                upd -> upd.hasCallbackQuery() && !"create_turn".equals(upd.getCallbackQuery().getData())
        );
    }
    public Reply handleCreationCallbackQueryReply() {
        return Reply.of(
                (bot, update) -> {
                    SendMessage sendMessage = new SendMessage();
                    Long id = update.getCallbackQuery().getMessage().getChatId();
                    sendMessage.setChatId(id);
                    sendMessage.setText("Введите название очереди");
                    chatActionService.addAction(id, ActionEturn.CREATE_TURN_NAME);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                },
                Flag.CALLBACK_QUERY,
                upd -> upd.hasCallbackQuery()
                        && "create_turn".equals(upd.getCallbackQuery().getData())
        );
    }

    public Reply handleCreationDescReply() {
        return Reply.of(
                (bot, update) -> {
                    Long id = update.getMessage().getChatId();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(id);
                    if (
                            chatActionService.getActionsEturn(id)
                                    == ActionEturn.CREATE_TURN_NAME
                    ) {
                        createTurnCommand.startCreating(id, update.getMessage().getText());
                        sendMessage.setText("Введите описание очереди");
                        chatActionService.addAction(id, ActionEturn.CREATE_TURN_DESCRIPTION);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (
                            chatActionService.getActionsEturn(id)
                                    == ActionEturn.CREATE_TURN_DESCRIPTION
                    ){
                        createTurnCommand.finishCreating(id, update.getMessage().getText(), silent);
                        chatActionService.clearAction(id);
                    } else {
                        regCommand.handle(update, bot.silent());
                    }
                },
                Flag.MESSAGE,
                upd -> !upd.getMessage().getText().startsWith("/")
        );
    }
}
