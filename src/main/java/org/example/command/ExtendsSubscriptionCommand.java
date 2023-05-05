package org.example.command;

public class ExtendsSubscriptionCommand {
    private final int clientId;
    private final int days;

    public ExtendsSubscriptionCommand(int clientId, int days) {
        this.clientId = clientId;
        this.days = days;
    }

    public int getClientId() {
        return clientId;
    }

    public int getDays() {
        return days;
    }
}
