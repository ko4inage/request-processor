package org.ko4inage.requestprocessor.repo;

import java.util.List;
import java.util.UUID;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<NotificationOutbox, UUID> {

  @Query("""
        select o from NotificationOutbox o
        where o.sent = false
        order by o.createdAt
        """)
  List<NotificationOutbox> findBatch(Pageable pageable);

  @Modifying
  @Query("""
        update NotificationOutbox n
        set n.attempt = n.attempt + 1
        where n.id = :id
        """)
  void incrementAttempt(@Param("id") UUID id);

}
