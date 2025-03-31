package com.eturn.telegram.repository;

import com.eturn.telegram.entity.LocalUser;
import com.eturn.telegram.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurnRepository extends JpaRepository<Turn, String> {
    boolean existsAllByHash(String hash);
    Optional<Turn> findByDoneAndCreator(boolean done, LocalUser creator);
}
