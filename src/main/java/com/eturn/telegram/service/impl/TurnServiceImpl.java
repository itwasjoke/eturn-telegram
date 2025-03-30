package com.eturn.telegram.service.impl;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.repository.TurnRepository;
import com.eturn.telegram.repository.UserRepository;
import com.eturn.telegram.service.TurnService;
import com.eturn.telegram.service.UserService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TurnServiceImpl implements TurnService {
    private final TurnRepository turnRepository;
    private final UserService userService;

    public TurnServiceImpl(TurnRepository turnRepository, UserService userService) {
        this.turnRepository = turnRepository;
        this.userService = userService;
    }

    @Override
    public String createTurn(Turn turn, Long userId) {
        String hash;
        int count = 0;

        do {
            hash = HashGenerator.generateUniqueCode();
            count++;
        } while (turnRepository.existsAllByHash(hash) && count <= 50);
        Optional<LocalUser> userOptional = userService.getUserById(userId);
        userOptional.ifPresent(turn::setCreator);
        turn.setHash(hash);
        return turnRepository.save(turn).getHash();
    }
}
