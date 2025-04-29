package org.example.fifa.rest.dto;

import org.example.fifa.model.Coach;

import java.util.Objects;

public class ClubStatistics {
    private String id;
    private String name;
    private String acronym;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
    private int yearCreation;
    private String stadium;
    private Coach coach;

    public ClubStatistics(String id, String name, String acronym, int rankingPoints, int scoredGoals, int concededGoals, int differenceGoals, int cleanSheetNumber, int yearCreation, String stadium, Coach coach) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.rankingPoints = rankingPoints;
        this.scoredGoals = scoredGoals;
        this.concededGoals = concededGoals;
        this.differenceGoals = differenceGoals;
        this.cleanSheetNumber = cleanSheetNumber;
        this.yearCreation = yearCreation;
        this.stadium = stadium;
        this.coach = coach;
    }

    public ClubStatistics(){};

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

    public int getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(int rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    public int getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(int scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public int getConcededGoals() {
        return concededGoals;
    }

    public void setConcededGoals(int concededGoals) {
        this.concededGoals = concededGoals;
    }

    public int getDifferenceGoals() {
        return differenceGoals;
    }

    public void setDifferenceGoals(int differenceGoals) {
        this.differenceGoals = differenceGoals;
    }

    public int getCleanSheetNumber() {
        return cleanSheetNumber;
    }

    public void setCleanSheetNumber(int cleanSheetNumber) {
        this.cleanSheetNumber = cleanSheetNumber;
    }

    public int getYearCreation() {
        return yearCreation;
    }

    public void setYearCreation(int yearCreation) {
        this.yearCreation = yearCreation;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubStatistics that = (ClubStatistics) o;
        return rankingPoints == that.rankingPoints && scoredGoals == that.scoredGoals && concededGoals == that.concededGoals && differenceGoals == that.differenceGoals && cleanSheetNumber == that.cleanSheetNumber && yearCreation == that.yearCreation && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(acronym, that.acronym) && Objects.equals(stadium, that.stadium) && Objects.equals(coach, that.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym, rankingPoints, scoredGoals, concededGoals, differenceGoals, cleanSheetNumber, yearCreation, stadium, coach);
    }

    @Override
    public String toString() {
        return "ClubStatisticDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", rankingPoints=" + rankingPoints +
                ", scoredGoals=" + scoredGoals +
                ", concededGoals=" + concededGoals +
                ", differenceGoals=" + differenceGoals +
                ", cleanSheetNumber=" + cleanSheetNumber +
                ", yearCreation=" + yearCreation +
                ", stadium='" + stadium + '\'' +
                ", coach=" + coach +
                '}';
    }
}
