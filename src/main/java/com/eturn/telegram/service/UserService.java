package com.eturn.telegram.service;

import com.eturn.telegram.entity.LocalUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

public interface UserService {
    LocalUser registerUser(Message message);
    Optional<LocalUser> getUserById(Long id);
}
