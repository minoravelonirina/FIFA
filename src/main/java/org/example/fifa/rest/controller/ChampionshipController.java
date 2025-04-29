package org.example.fifa.rest.controller;

import org.example.fifa.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class ChampionshipController {
    @Autowired private ChampionshipService championshipService;
}
