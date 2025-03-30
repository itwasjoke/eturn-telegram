package com.eturn.telegram.service.impl;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.repository.UserRepository;
import com.eturn.telegram.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LocalUser registerUser(Message message) {
        User TgUser = message.getFrom();
        // если существует, возвращаем текущий
        Optional<LocalUser> localUserOptional = getUserById(TgUser.getId());
        if (localUserOptional.isPresent()) return localUserOptional.get();

        // иначе создаем нового
        LocalUser localUser = new LocalUser();
        localUser.setId(TgUser.getId());
        localUser.setUsername(TgUser.getUserName());
        localUser.setFirstName(TgUser.getFirstName());
        localUser.setLastName(TgUser.getLastName());
        return userRepository.save(localUser);
    }

    @Override
    public Optional<LocalUser> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
