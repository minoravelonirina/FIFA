package org.example.fifa.service;

import org.example.fifa.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class ChampionshipService {
    @Autowired private ChampionshipRepository championshipRepository;
}
