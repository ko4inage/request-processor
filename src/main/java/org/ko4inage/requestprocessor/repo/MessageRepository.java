package org.ko4inage.requestprocessor.repo;

import java.util.UUID;
import org.ko4inage.requestprocessor.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
