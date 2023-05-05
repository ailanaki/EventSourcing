package org.example.controllers;


import org.assertj.core.api.Assertions;
import org.example.command.CreateSubscriptionCommand;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.statistics.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class AdminControllerTest {
    @Autowired
    private AdminController adminController;

    @Test
    public void createClient() {
        long clientId =adminController.createClient(new CreateSubscriptionCommand("name"));

        Subscription subscription = adminController.getSubscription(Math.toIntExact(clientId));
        Assertions.assertThat(subscription.getClientName()).isEqualTo("name");
        Assertions.assertThat(subscription.getValidUntil()).isNull();
    }

    @Test
    public void extendSubscription() {
        LocalDateTime currentTime = LocalDateTime.now();
        long clientId = adminController.createClient(new CreateSubscriptionCommand("name_2"));
        ExtendsSubscriptionCommand command = new ExtendsSubscriptionCommand(Math.toIntExact(clientId), 5);
        adminController.extendSubscription(command);

        Subscription subscription = adminController.getSubscription(Math.toIntExact(clientId));
        Assertions.assertThat(subscription.getClientName()).isEqualTo("name_2");
        Assertions.assertThat(subscription.getValidUntil()).isAfter(currentTime.plus(5, ChronoUnit.DAYS));
        Assertions.assertThat(subscription.getValidUntil()).isBefore(currentTime.plus(6, ChronoUnit.DAYS));
    }

    @Test
    public void extendSubscriptionTwice() {
        LocalDateTime currentTime = LocalDateTime.now();
        long clientId = adminController.createClient(new CreateSubscriptionCommand("name_3"));
        ExtendsSubscriptionCommand command = new ExtendsSubscriptionCommand((int) clientId, 5);
        adminController.extendSubscription(command);
        adminController.extendSubscription(command);

        Subscription subscription = adminController.getSubscription((int) clientId);
        Assertions.assertThat(subscription.getClientName()).isEqualTo("name_3");
        Assertions.assertThat(subscription.getValidUntil()).isAfter(currentTime.plus(10, ChronoUnit.DAYS));
        Assertions.assertThat(subscription.getValidUntil()).isBefore(currentTime.plus(11, ChronoUnit.DAYS));
    }
}