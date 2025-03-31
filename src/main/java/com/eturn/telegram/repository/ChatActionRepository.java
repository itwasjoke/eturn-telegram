package com.eturn.telegram.repository;

import com.eturn.telegram.entity.ChatAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatActionRepository extends JpaRepository<ChatAction, Long> {
}
