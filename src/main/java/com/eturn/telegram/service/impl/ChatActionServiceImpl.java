package com.eturn.telegram.service.impl;

import com.eturn.telegram.entity.ChatAction;
import com.eturn.telegram.enums.ActionEturn;
import com.eturn.telegram.repository.ChatActionRepository;
import com.eturn.telegram.service.ChatActionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatActionServiceImpl implements ChatActionService {
    private final ChatActionRepository chatActionRepository;

    public ChatActionServiceImpl(ChatActionRepository chatActionRepository) {
        this.chatActionRepository = chatActionRepository;
    }

    @Override
    public ActionEturn getActionsEturn(Long chatId) {
        Optional<ChatAction> chatAction = chatActionRepository.findById(chatId);
        if (chatAction.isPresent()) {
            return chatAction.get().getAction();
        }
        return null;
    }

    @Override
    public void clearAction(Long chatId) {
        chatActionRepository.deleteById(chatId);
    }

    @Override
    public void addAction(Long chatId, ActionEturn action) {
        Optional<ChatAction> chatAction = chatActionRepository.findById(chatId);
        if (chatAction.isPresent()) {
            ChatAction actionToAdd = chatAction.get();
            actionToAdd.setAction(action);
            chatActionRepository.save(actionToAdd);
        } else {
            ChatAction newChatAction = new ChatAction();
            newChatAction.setAction(action);
            newChatAction.setChatId(chatId);
            chatActionRepository.save(newChatAction);
        }

    }
}
