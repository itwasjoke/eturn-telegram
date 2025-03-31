package com.eturn.telegram.service.impl;

import com.eturn.telegram.dto.TurnWithPositionsDTO;
import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.repository.TurnRepository;
import com.eturn.telegram.service.PositionService;
import com.eturn.telegram.service.TurnService;
import com.eturn.telegram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TurnServiceImpl implements TurnService {
    private final TurnRepository turnRepository;
    private final UserService userService;
    private final PositionService positionService;

    public TurnServiceImpl(TurnRepository turnRepository, UserService userService, PositionService positionService) {
        this.turnRepository = turnRepository;
        this.userService = userService;
        this.positionService = positionService;
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
        Optional<LocalUser> userOptional = userService.getOptionalUserById(userId);
        userOptional.ifPresent(turn::setCreator);
        turn.setHash(hash);
        turn.setDone(false);
        turnRepository.save(turn);
    }

    @Override
    public Turn finishCreating(String desc, Long userId) {
        Optional<LocalUser> userOptional =
                userService.getOptionalUserById(userId);
        if (userOptional.isPresent()) {
            Optional<Turn> turnOptional =
                    turnRepository.findByDoneAndCreator(
                            false,
                            userOptional.get()
                    );
            if (turnOptional.isPresent()) {
                Turn turn = turnOptional.get();
                turn.setDone(true);
                turn.setCreator(userOptional.get());
                turn.setDescription(desc);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                turn.setDateStart(calendar.getTime());
                return turnRepository.save(turn);
            }
        }
        return null;
    }

    @Override
    public Optional<Turn> getTurn(String id) {
        return turnRepository.findById(id);
    }

    @Override
    public Optional<TurnWithPositionsDTO> getTurnWithPositions(String id, Long userId) {
        Optional<Turn> turnOptional = turnRepository.findById(id);
        if (turnOptional.isPresent()) {
            Turn turn = turnOptional.get();
            return Optional.of(
                    new TurnWithPositionsDTO(
                            turn,
                            positionService.findFirstByUser(turn, userId),
                            positionService.findCurrentByTurn(turn, userId)
                    )
            );
        }
        return Optional.empty();
    }

    @Override
    public List<Turn> findTurns(Long userId) {
        LocalUser user = userService.getUserById(userId);
        Set<Turn> turns = turnRepository.findAllByPositions_LocalUser(user);
        Set<Turn> turnsCreated = turnRepository.findAllByCreator(user);
        Set<Turn> allTurns = new HashSet<>();
        allTurns.addAll(turnsCreated);
        allTurns.addAll(turns);
        return allTurns.stream().toList();
    }

    @Override
    public boolean turnExists(LocalUser user) {
        List<Turn> turns = turnRepository.findAll();
        for (Turn turn : turns) {
            log.info(turn.getHash()+" "+turn.getCreator().getId());
            log.info(user.getId().toString());
        }
        boolean existsForCreator = turnRepository.existsByCreator(user);
        boolean existsForPositions = positionService.existsByUser(user);
        log.info(existsForCreator + " " + existsForPositions);
        return existsForPositions || existsForCreator;
    }
}
