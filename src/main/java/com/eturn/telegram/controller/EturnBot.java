package com.eturn.telegram.controller;

import com.eturn.telegram.controller.command.ButtonsHandler;
import com.eturn.telegram.controller.command.RegCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component("eturnBot")
@Log4j2
public class EturnBot extends AbilityBot {

    private final RegCommand regCommand;
    private final ButtonsHandler buttonsHandler;

    protected EturnBot(
            @Value("${token}") String botToken,
            @Value("${bot.name}") String botUsername,
            RegCommand regCommand,
            ButtonsHandler buttonsHandler
    ) {
        super(botToken, botUsername);
        this.regCommand = regCommand;
        this.buttonsHandler = buttonsHandler;
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
                .action(ctx -> regCommand.getAnswer(ctx.update().getMessage(), this.silent))
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
                    log.info(update.getCallbackQuery().getData());
                    buttonsHandler.handleCreation(bot.silent()).action().accept(bot, update);
                },
                Flag.CALLBACK_QUERY,
                upd -> upd.hasCallbackQuery() && "create_turn".equals(upd.getCallbackQuery().getData())
        );
    }
}
