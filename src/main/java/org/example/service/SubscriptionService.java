package org.example.service;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.events.ExtendsSubscriptionEvent;
import org.example.repository.ExtendsSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class SubscriptionService {

    private final ExtendsSubscriptionRepository subscriptionRepository;

    public SubscriptionService(ExtendsSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void extendSubscription(ExtendsSubscriptionCommand extension) {
        ExtendsSubscriptionEvent entity = new ExtendsSubscriptionEvent();
        entity.setClientId(extension.getClientId());
        entity.setDays(extension.getDays());
        subscriptionRepository.save(entity);
    }

    public LocalDateTime getSubscriptionEndTime(int clientId) {
        List<ExtendsSubscriptionEvent> extensions = subscriptionRepository.findAllByClientId(clientId);
        if (extensions.isEmpty()) {
            return null;
        }

        extensions.sort(Comparator.comparing(ExtendsSubscriptionEvent::getCreated));
        ExtendsSubscriptionEvent first = extensions.get(0);
        LocalDateTime res = first.getCreated().plusDays(first.getDays());
        for (int i = 1; i < extensions.size(); i++) {
            ExtendsSubscriptionEvent curExt = extensions.get(i);
            LocalDateTime curStart = curExt.getCreated();
            if (curStart.isAfter(res)) {
                res = curStart.plusDays(curExt.getDays());
            } else {
                res = res.plusDays(curExt.getDays());
            }
        }
        return res;
    }
}