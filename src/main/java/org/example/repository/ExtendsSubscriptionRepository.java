package org.example.repository;

import org.example.events.ExtendsSubscriptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtendsSubscriptionRepository extends JpaRepository<ExtendsSubscriptionEvent, Integer> {
    List<ExtendsSubscriptionEvent> findAllByClientId(int clientId);
}