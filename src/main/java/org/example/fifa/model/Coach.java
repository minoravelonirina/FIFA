package org.example.fifa.model;

import java.util.Objects;

public class Coach {
    private Long id;
    private String coachName;
    private String nationality;

    public Coach(Long id, String coachName, String nationality) {
        this.id = id;
        this.coachName = coachName;
        this.nationality = nationality;
    }

    public Coach(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(id, coach.id) && Objects.equals(coachName, coach.coachName) && Objects.equals(nationality, coach.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coachName, nationality);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", coachName='" + coachName + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
