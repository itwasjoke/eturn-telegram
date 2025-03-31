package com.eturn.telegram.service.impl;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.repository.UserRepository;
import com.eturn.telegram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LocalUser registerUser(User TgUser) {
        log.info("USER INFO");
        log.info("USER INFO");
        log.info("USER INFO " + TgUser.getId());
        log.info("USER INFO");
        log.info("USER INFO");
        // если существует, возвращаем текущий
        Optional<LocalUser> localUserOptional = this.getOptionalUserById(TgUser.getId());
        if (localUserOptional.isPresent()) return localUserOptional.get();

        // иначе создаем нового
        LocalUser localUser = new LocalUser();
        localUser.setId(TgUser.getId());
        localUser.setTurns(new HashSet<>());
        localUser.setUsername(TgUser.getUserName());
        localUser.setFirstName(TgUser.getFirstName());
        localUser.setLastName(TgUser.getLastName());
        return userRepository.save(localUser);
    }

    @Override
    public Optional<LocalUser> getOptionalUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public LocalUser getUserById(Long id) {
        Optional<LocalUser> userOptional = this.getOptionalUserById(id);
        return userOptional.orElse(null);
    }
}
