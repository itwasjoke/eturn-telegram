package com.eturn.telegram.service.impl;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.repository.TurnRepository;
import com.eturn.telegram.repository.UserRepository;
import com.eturn.telegram.service.TurnService;
import com.eturn.telegram.service.UserService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
    public void startCreating(String name, Long userId) {
        Turn turn = new Turn();
        turn.setName(name);
        String hash;
        int count = 0;

        do {
            hash = HashGenerator.generateUniqueCode();
            count++;
        } while (turnRepository.existsAllByHash(hash) && count <= 50);
        Optional<LocalUser> userOptional = userService.getUserById(userId);
        userOptional.ifPresent(turn::setCreator);
        turn.setHash(hash);
        turn.setDone(false);
        turnRepository.save(turn);
    }

    @Override
    public Turn finishCreating(String desc, Long userId) {
        Optional<LocalUser> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            Optional<Turn> turnOptional = turnRepository.findByDoneAndCreator(false,userOptional.get());
            if (turnOptional.isPresent()) {
                Turn turn = turnOptional.get();
                turn.setDone(true);
                turn.setDescription(desc);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                turn.setDateStart(calendar.getTime());
                return turnRepository.save(turn);
            }
        }
        return null;
    }
}
