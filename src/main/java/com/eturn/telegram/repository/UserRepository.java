package com.eturn.telegram.repository;

import com.eturn.telegram.entity.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<LocalUser, Long> {
}
