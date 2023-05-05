package org.example.controllers;

import org.example.command.CreateSubscriptionCommand;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.events.ExtendsSubscriptionEvent;
import org.example.repository.CreateSubsriptionRepository;
import org.example.repository.ExtendsSubscriptionRepository;
import org.example.repository.TurnstileEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TurnstileControllerTest {

    @Autowired
    private AdminController admin;
    @Autowired
    private TurnstileController controller;
    @Autowired
    private TurnstileEventRepository trep;
    @Autowired
    private ExtendsSubscriptionRepository srep;
    @Autowired
    private CreateSubsriptionRepository crep;
    private int validClient;
    private int invalidClient;

    @BeforeEach
    public void setUp() {
        validClient = admin.createClient(new CreateSubscriptionCommand("valid"));
        invalidClient = admin.createClient(new CreateSubscriptionCommand("invalid"));
        ExtendsSubscriptionCommand ext = new ExtendsSubscriptionCommand(validClient, 10);
        admin.extendSubscription(ext);
    }

    @AfterEach
    public void cleanUp() {
        trep.deleteAll();
        srep.deleteAll();
        crep.deleteAll();
    }

    @Test
    public void testIn() {
        assertThatNoException().isThrownBy(() -> controller.in(validClient));
    }

    @Test
    public void testInTwice() {
        controller.in(validClient);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> controller.in(validClient))
                .withMessage("Already in");
    }

    @Test
    public void testOut() {
        controller.in(validClient);
        assertThatNoException().isThrownBy(() -> controller.out(validClient));
    }

    @Test
    public void testOutFirst() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> controller.out(validClient))
                .withMessage("Already out");
    }

    @Test
    public void testOutTwice() {
        controller.in(validClient);
        controller.out(validClient);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> controller.out(validClient))
                .withMessage("Already out");
    }

    @Test
    public void testInBad() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> controller.in(invalidClient))
                .withMessage("Subscription ended");
    }

    @Test
    public void testInSubscriptionEnded() {
        ExtendsSubscriptionEvent entity = new ExtendsSubscriptionEvent();
        entity.setClientId(invalidClient);
        entity.setDays(4);
        ExtendsSubscriptionEvent save = srep.save(entity);
        save.setCreated(LocalDateTime.now().minusDays(5));
        srep.save(entity);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> controller.in(invalidClient))
                .withMessage("Subscription ended");
    }

    @Test
    public void testInSubscriptionEndedAndExtended() {
        ExtendsSubscriptionEvent entity = new ExtendsSubscriptionEvent();
        entity.setClientId(invalidClient);
        entity.setDays(4);
        ExtendsSubscriptionEvent save = srep.save(entity);
        save.setCreated(LocalDateTime.now().minusDays(50));
        srep.save(entity);

        ExtendsSubscriptionCommand ext = new ExtendsSubscriptionCommand(invalidClient, 4);
        admin.extendSubscription(ext);

        assertThatNoException().isThrownBy(() -> controller.in(invalidClient));
    }
}