package org.ko4inage.requestprocessor.repo;

import org.ko4inage.requestprocessor.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
