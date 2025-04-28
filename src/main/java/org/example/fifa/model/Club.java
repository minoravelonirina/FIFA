package org.example.fifa.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Club {
    private Long id;
    private String clubName;
    private String acronym;
    private LocalDate creationDate;
    private String stadium;
    private Coach coach;
    private List<Player> players;

    public Club(Long id, String clubName, String acronym, LocalDate creationDate, String stadium, Coach coach, List<Player> players) {
        this.id = id;
        this.clubName = clubName;
        this.acronym = acronym;
        this.creationDate = creationDate;
        this.stadium = stadium;
        this.coach = coach;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return Objects.equals(id, club.id) && Objects.equals(clubName, club.clubName) && Objects.equals(acronym, club.acronym) && Objects.equals(creationDate, club.creationDate) && Objects.equals(stadium, club.stadium) && Objects.equals(coach, club.coach) && Objects.equals(players, club.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clubName, acronym, creationDate, stadium, coach, players);
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", clubName='" + clubName + '\'' +
                ", acronym='" + acronym + '\'' +
                ", creationDate=" + creationDate +
                ", stadium='" + stadium + '\'' +
                ", coach=" + coach +
                ", players=" + players +
                '}';
    }
}
