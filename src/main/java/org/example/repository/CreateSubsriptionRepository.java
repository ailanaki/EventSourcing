package org.example.repository;

import org.example.events.CreateSubscriptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateSubsriptionRepository extends JpaRepository<CreateSubscriptionEvent, Integer> {
}