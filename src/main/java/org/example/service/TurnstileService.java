package org.example.service;

import org.example.events.TurnstileEvent;
import org.example.repository.TurnstileEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TurnstileService {

    private final TurnstileEventRepository turnstileEventRepository;
    private final SubscriptionService subscriptionService;

    public TurnstileService(TurnstileEventRepository turnstileEventRepository, SubscriptionService subscriptionService) {
        this.turnstileEventRepository = turnstileEventRepository;
        this.subscriptionService = subscriptionService;
    }

    public void letIn(int clientId) {
        LocalDateTime end = subscriptionService.getSubscriptionEndTime(clientId);
        if (end == null || end.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Subscription ended");
        }
        List<TurnstileEvent> events = turnstileEventRepository.findAllByClientId(clientId);
        events.sort(Comparator.comparing(TurnstileEvent::getCreated).reversed());
        if (!events.isEmpty() && events.get(0).getDirection() == TurnstileEvent.Direction.IN) {
            throw new RuntimeException("Already in");
        }
        TurnstileEvent entity = new TurnstileEvent();
        entity.setDirection(TurnstileEvent.Direction.IN);
        entity.setClientId(clientId);
        turnstileEventRepository.save(entity);
    }

    public void letOut(int clientId) {
        List<TurnstileEvent> events = turnstileEventRepository.findAllByClientId(clientId);
        events.sort(Comparator.comparing(TurnstileEvent::getCreated).reversed());
        if (events.isEmpty() || events.get(0).getDirection() == TurnstileEvent.Direction.OUT) {
            throw new RuntimeException("Already out");
        }
        TurnstileEvent entity = new TurnstileEvent();
        entity.setDirection(TurnstileEvent.Direction.OUT);
        entity.setClientId(clientId);
        turnstileEventRepository.save(entity);
    }
}
