package org.example.fifa.rest.dto;

import org.example.fifa.model.Club;
import org.example.fifa.model.Coach;

import java.time.LocalDate;
import java.util.Objects;

public class ClubDto {
    private String id;
    private String name;
    private String acronym;
    private int yearCreation;
    private String stadium;
    private Coach coach;

    public ClubDto(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.acronym = club.getAcronym();
        this.yearCreation = club.getYearCreation();
        this.stadium = club.getStadium();
        this.coach = club.getCoach();
    }

    public ClubDto(String id, String name, String acronym, int yearCreation, String stadium, Coach coach) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = yearCreation;
        this.stadium = stadium;
        this.coach = coach;
    }

    public ClubDto(){};

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
        ClubDto clubDto = (ClubDto) o;
        return Objects.equals(id, clubDto.id) && Objects.equals(name, clubDto.name) && Objects.equals(acronym, clubDto.acronym) && Objects.equals(yearCreation, clubDto.yearCreation) && Objects.equals(stadium, clubDto.stadium) && Objects.equals(coach, clubDto.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, acronym, yearCreation, stadium, coach);
    }

    @Override
    public String toString() {
        return "ClubDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", creationDate=" + yearCreation +
                ", stadium='" + stadium + '\'' +
                ", coach=" + coach +
                '}';
    }
}
