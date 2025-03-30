package com.eturn.telegram.controller;

import com.eturn.telegram.controller.command.RegCommand;
import com.eturn.telegram.controller.command.TgCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Log4j2
public class BotController {

//    @Value("${token}")
//    private String botToken;
//    private final RegCommand regCommand;
//
//    public BotController(
//             RegCommand regCommand
//    ) {
//        log.info("BotController created");
//        log.info("BotController token: " + botToken);
//        this.regCommand = regCommand;
//    }
//
//
//    private TgCommand getCommand(Message message){
//        switch (message.getText().split(" ")[0]) {
//            case "/start":
//                return regCommand;
//        }
//        return null;
//    }
//
//    @Override
//    public String getBotUsername() {
//        return "eturn_bot";
//    }
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//            SendMessage message = new SendMessage();
//            message.setChatId(String.valueOf(chatId));
//            message.setText("Вы написали: " + messageText);
//
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            Message message = update.getMessage();
//            TgCommand tgCommand = getCommand(message);
//            SendMessage sendMessage = null;
//            if (tgCommand != null) {
//                sendMessage = tgCommand.getAnswer(message);
//            }
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
