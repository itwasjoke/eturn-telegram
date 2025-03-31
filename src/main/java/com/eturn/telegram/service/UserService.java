package com.eturn.telegram.service;

import com.eturn.telegram.entity.LocalUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public interface UserService {
    LocalUser registerUser(User TgUser);
    Optional<LocalUser> getOptionalUserById(Long id);
    LocalUser getUserById(Long id);
}
