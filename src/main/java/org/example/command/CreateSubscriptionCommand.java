package org.example.command;

public class CreateSubscriptionCommand {
    private final String name;
    public CreateSubscriptionCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}