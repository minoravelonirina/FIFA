package org.example.fifa.rest.dto;

import java.util.List;
import java.util.Objects;

public class ClubWithGoalsDto {
    private String id;
    private String name;
    private String acronym;
    private List<ScorerDto> scorers;

    public ClubWithGoalsDto(String id, String name, String acronym, List<ScorerDto> scorers) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
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
        if (scorers == null) {
            return 1;
        }
        return scorers.stream()
                .mapToInt(scorer -> scorer.isOwnGoal() ? 0 : 3)
                .sum();
    }


    public int getScoredGoals() {
        // Nombre de buts marqués par le club (but non contre son camp)
        return (int) scorers.stream()
                .filter(scorer -> !scorer.isOwnGoal())
                .count();
    }

    public int getConcededGoals() {
        // Nombre de buts contre son camp (own goal) pour ce club
        return (int) scorers.stream()
                .filter(ScorerDto::isOwnGoal)
                .count();
    }

    public int getDifferenceGoals() {
        return getScoredGoals() - getConcededGoals();
    }

    public int getCleanSheetNumber() {
        return getConcededGoals() == 0 ? 1 : 0;
    }

    public int getRankingPoints() {
        return 0;
    }

    public void setScore(int score) {};

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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(acronym, that.acronym) && Objects.equals(scorers, that.scorers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym, scorers);
    }

    @Override
    public String toString() {
        return "ClubWithGoalsDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", score=" + getScore() +
                ", scorers=" + scorers +
                '}';
    }
}
