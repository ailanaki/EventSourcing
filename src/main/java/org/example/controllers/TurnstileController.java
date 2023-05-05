package org.example.controllers;

import org.example.service.TurnstileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/turnstile")
public class TurnstileController {

    private final TurnstileService service;

    public TurnstileController(TurnstileService service) {
        this.service = service;
    }

    @PostMapping("/in/{id}")
    public void in(@PathVariable int id) {
        service.letIn(id);
    }

    @PostMapping("/out/{id}")
    public void out(@PathVariable int id) {
        service.letOut(id);
    }

}