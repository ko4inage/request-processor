package org.ko4inage.requestprocessor.repo;

import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationOutbox, UUID> {

    @Query("""
        select o from NotificationOutbox o
        where o.sent = false
        order by o.createdAt asc
    """)
    List<NotificationOutbox> findBatch(Pageable pageable);

}
