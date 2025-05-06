package org.example.fifa.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class WelcomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> welcome() {
        String banner = """
                ===========================================
                  🎮 Bienvenue sur l'API CHAMPIONNAT 🎮
                ===========================================
                """;
        return ResponseEntity.ok().body(banner);
    }
}

