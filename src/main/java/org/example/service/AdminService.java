package org.example.service;


import org.example.command.CreateSubscriptionCommand;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.events.CreateSubscriptionEvent;
import org.example.repository.CreateSubsriptionRepository;
import org.example.statistics.Subscription;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final CreateSubsriptionRepository clientRepository;
    private final SubscriptionService subscriptionService;

    public AdminService(CreateSubsriptionRepository clientRepository, SubscriptionService subscriptionService) {
        this.clientRepository = clientRepository;
        this.subscriptionService = subscriptionService;
    }

    public Integer registerClient(CreateSubscriptionCommand command) {
        CreateSubscriptionEvent entity = new CreateSubscriptionEvent();
        entity.setName(command.getName());
        return clientRepository.save(entity).getId();
    }

    public void extendSubscription(ExtendsSubscriptionCommand extension) {
        subscriptionService.extendSubscription(extension);
    }

    public Subscription getSubscription(int clientId) {
        CreateSubscriptionEvent client = clientRepository.findById(clientId).orElseThrow();
        return new Subscription(client.getName(), subscriptionService.getSubscriptionEndTime(clientId));
    }

}
