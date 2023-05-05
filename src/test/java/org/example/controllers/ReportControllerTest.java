package org.example.controllers;

import org.example.events.TurnstileEvent;
import org.example.statistics.AverageStatistics;
import org.junit.jupiter.api.AfterEach;
import org.example.command.CreateSubscriptionCommand;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.repository.CreateSubsriptionRepository;
import org.example.repository.ExtendsSubscriptionRepository;
import org.example.repository.TurnstileEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class ReportControllerTest {
    @Autowired
    private AdminController admin;
    @Autowired
    private ReportController controller;
    @Autowired
    private TurnstileEventRepository trep;
    @Autowired
    private ExtendsSubscriptionRepository srep;
    @Autowired
    private CreateSubsriptionRepository crep;
    private int client1;
    private int client2;

    @BeforeEach
    public void setUp() {
        client1 = admin.createClient(new CreateSubscriptionCommand("client1"));
        client2 = admin.createClient(new CreateSubscriptionCommand("client2"));
        ExtendsSubscriptionCommand ext = new ExtendsSubscriptionCommand(client1, 10);
        admin.extendSubscription(ext);
        ext = new ExtendsSubscriptionCommand(client2, 10);
        admin.extendSubscription(ext);
    }

    @AfterEach
    public void cleanUp() {
        trep.deleteAll();
        srep.deleteAll();
        crep.deleteAll();
    }

    @Test
    public void testDayStatistics() {
        addVisit(3, client1);
        addVisit(3, client2);
        addVisit(5, client1);

        Map<LocalDateTime, Integer> stat = controller.getDayStatistics(6);
        assertThat(stat)
                .containsEntry(LocalDateTime.now().minusDays(3).withHour(0).withMinute(0).withSecond(0).withNano(0), 2)
                .containsEntry(LocalDateTime.now().minusDays(5).withHour(0).withMinute(0).withSecond(0).withNano(0), 1);
    }

    @Test
    public void testAverageEmpty() {
        AverageStatistics stat = controller.averageStatistics();
        assertThat(stat.getVisitsPerDay()).isEqualTo(0);
        assertThat(stat.getAverageVisitMinutes()).isEqualTo(0);
    }

    @Test
    public void testAverage() {
        addVisit(3, client1);
        addVisit(3, client2);
        addVisit(5, client1);

        AverageStatistics stat = controller.averageStatistics();
        assertThat(stat.getVisitsPerDay()).isEqualTo(3.0 / 5);
        assertThat(stat.getAverageVisitMinutes()).isEqualTo(24 * 60);
    }

    @Test
    public void testAverageCaching() {
        addVisit(3, client1);
        addVisit(3, client2);
        addVisit(5, client1);

        controller.averageStatistics();
        addIn(0, client1);
        addOut(0, client1);

        addIn(0, client2);
        addOut(0, client2);
        AverageStatistics stat = controller.averageStatistics();

        assertThat(stat.getVisitsPerDay()).isEqualTo(5.0 / 5);
        assertThat(stat.getAverageVisitMinutes()).isEqualTo(24.0 * 60 * 3 / 5);
    }

    private void addIn(int daysAgo, int clientId) {
        TurnstileEvent entity = new TurnstileEvent();
        entity.setClientId(clientId);
        entity.setDirection(TurnstileEvent.Direction.IN);
        TurnstileEvent save = trep.save(entity);
        save.setCreated(LocalDateTime.now().minusDays(daysAgo));
        trep.save(save);
    }

    private void addOut(int daysAgo, int clientId) {
        TurnstileEvent entity = new TurnstileEvent();
        entity.setClientId(clientId);
        entity.setDirection(TurnstileEvent.Direction.OUT);
        TurnstileEvent save = trep.save(entity);
        save.setCreated(LocalDateTime.now().minusDays(daysAgo));
        trep.save(save);
    }

    private void addVisit(int daysAgo, int clientId) {
        addIn(daysAgo, clientId);
        addOut(daysAgo - 1, clientId);
    }
}