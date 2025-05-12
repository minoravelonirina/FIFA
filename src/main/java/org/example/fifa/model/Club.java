package org.example.fifa.model;

import org.example.fifa.model.enums.Championship;

import java.util.List;
import java.util.Objects;

public class Club {
    private String id;
    private String name;
    private String acronym;
    private int yearCreation;
    private String stadium;
    private Coach coach;
    private List<Player> players;
    private Championship championship;

    public Club(String id, String name, String acronym, int creationDate, String stadium, Coach coach, List<Player> players) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = creationDate;
        this.stadium = stadium;
        this.coach = coach;
        this.players = players;
    }

    public Club(String id, String name, String acronym, int yearCreation, String stadium, Coach coach, List<Player> players, Championship championship) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = yearCreation;
        this.stadium = stadium;
        this.coach = coach;
        this.players = players;
        this.championship = championship;
    }

    public Club(String id, String name, String acronym, int creationDate, String stadium, Coach coach) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = creationDate;
        this.stadium = stadium;
        this.coach = coach;
    }

    public Club(){};
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getAcronym() {return acronym;}
    public void setAcronym(String acronym) {this.acronym = acronym;}
    public int getYearCreation() {return yearCreation;}
    public void setYearCreation(int creationDate) {this.yearCreation = creationDate;}
    public String getStadium() {return stadium;}
    public void setStadium(String stadium) {this.stadium = stadium;}
    public Coach getCoach() {return coach;}
    public void setCoach(Coach coach) {this.coach = coach;}
    public List<Player> getPlayers() {return players;}
    public void setPlayers(List<Player> players) {this.players = players;}
    public Championship getChampionship(){return this.championship;};
    public void setChampionship(Championship championship){this.championship = championship;};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return Objects.equals(id, club.id) && Objects.equals(name, club.name) && Objects.equals(acronym, club.acronym) && Objects.equals(yearCreation, club.yearCreation) && Objects.equals(stadium, club.stadium) && Objects.equals(coach, club.coach) && Objects.equals(players, club.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym, yearCreation, stadium, coach, players);
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", yearCreation=" + yearCreation +
                ", stadium='" + stadium + '\'' +
                ", coach=" + coach +
                ", players=" + players +
                '}';
    }
}
