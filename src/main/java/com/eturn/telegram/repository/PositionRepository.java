package com.eturn.telegram.repository;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Position;
import com.eturn.telegram.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findFirstByLocalUserAndTurn(LocalUser localUser, Turn turn);
    Optional<Position> findFirstByTurn(Turn turn);
    Integer countByLocalUserAndTurnAndIdLessThan(LocalUser localUser, Turn turn, Long id);
    boolean existsByLocalUser(LocalUser localUser);
}
