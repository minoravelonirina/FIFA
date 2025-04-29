package org.example.fifa.rest.dto;

import org.example.fifa.model.Coach;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ClubWithGoalsDto {
    private String id;
    private String name;
    private String acronym;
    private int score;
    private List<ScorerDto> scorers;

    public ClubWithGoalsDto(String id, String name, String acronym, int score, List<ScorerDto> scorers) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.score = score;
        this.scorers = scorers;
    }

    public ClubWithGoalsDto(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ScorerDto> getScorers() {
        return scorers;
    }

    public void setScorers(List<ScorerDto> scorers) {
        this.scorers = scorers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubWithGoalsDto that = (ClubWithGoalsDto) o;
        return score == that.score && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(acronym, that.acronym) && Objects.equals(scorers, that.scorers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym, score, scorers);
    }

    @Override
    public String toString() {
        return "ClubWithGoalsDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", score=" + score +
                ", scorers=" + scorers +
                '}';
    }
}
