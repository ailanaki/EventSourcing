package org.example.statistics;
import java.time.Instant;
import java.time.LocalDateTime;

public class Subscription {
    private final String clientName;
    private final LocalDateTime validUntil;

    public Subscription(final String clientName, final LocalDateTime validUntil) {
        this.clientName = clientName;
        this.validUntil = validUntil;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }
}