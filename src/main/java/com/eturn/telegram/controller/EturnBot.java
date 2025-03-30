package com.eturn.telegram.controller;

import com.eturn.telegram.controller.command.RegCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import javax.swing.*;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component("eturnBot")
public class EturnBot extends AbilityBot {

    private final RegCommand regCommand;

    protected EturnBot(
            @Value("${token}") String botToken,
            @Value("${bot.name}") String botUsername,
           RegCommand regCommand
    ) {
        super(botToken, botUsername);
        this.regCommand = regCommand;
        regCommand.setSilentSender(silent);
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
                .action(ctx -> regCommand.getAnswer(ctx.update().getMessage()))
                .build();
    }
}
