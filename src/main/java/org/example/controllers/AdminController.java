package org.example.controllers;

import org.example.command.CreateSubscriptionCommand;
import org.example.command.ExtendsSubscriptionCommand;
import org.example.service.AdminService;
import org.example.statistics.Subscription;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public int createClient(@RequestBody CreateSubscriptionCommand command) {
        return service.registerClient(command);
    }

    @PostMapping("/extend")
    public void extendSubscription(@RequestBody ExtendsSubscriptionCommand command) {
        service.extendSubscription(command);
    }

    @GetMapping("/subscription/{id}")
    public Subscription getSubscription(@PathVariable int id) {
        return service.getSubscription(id);
    }

}