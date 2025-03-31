package com.eturn.telegram.service.impl;

import com.eturn.telegram.dto.PositionDTO;
import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;
import com.eturn.telegram.repository.PositionRepository;
import com.eturn.telegram.service.PositionService;
import com.eturn.telegram.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final UserService userService;

    public PositionServiceImpl(PositionRepository positionRepository, UserService userService) {
        this.positionRepository = positionRepository;
        this.userService = userService;
    }

    @Override
    public Optional<PositionDTO> findFirstByUser(Turn turn, Long userId) {
        LocalUser user = userService.getUserById(userId);
        Optional<Position> positionOptional
                = positionRepository.findFirstByLocalUserAndTurn(user, turn);
        if (positionOptional.isPresent()) {
            Position position = positionOptional.get();
            return Optional.of(
                    new PositionDTO(
                            position.getLocalUser().getFirstName()+" "+position.getLocalUser().getLastName(),
                            positionRepository.countByLocalUserAndTurnAndIdLessThan(user, turn, position.getId()),
                            position.getDateStarted(),
                            position.getStarted()
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PositionDTO> findCurrentByTurn(Turn turn, Long userId) {
        if (turn.getCreator().getId().equals(userId)) {
            return Optional.empty();
        }
        Optional<Position> positionOptional
                = positionRepository.findFirstByTurn(turn);
        if (positionOptional.isPresent()) {
            Position position = positionOptional.get();
            return Optional.of(
                    new PositionDTO(
                            position.getLocalUser().getFirstName()+" "+position.getLocalUser().getLastName(),
                            1,
                            position.getDateStarted(),
                            position.getStarted()
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUser(LocalUser user) {
        return positionRepository.existsByLocalUser(user);
    }
}
