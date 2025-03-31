package com.eturn.telegram.entity;

import com.eturn.telegram.enums.ActionEturn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatAction {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private ActionEturn action;
}
