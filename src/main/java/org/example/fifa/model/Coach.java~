package org.example.fifa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Coach {
    private String id;
    private String name;
    private String nationality;

    public Coach(String id, String coachName, String nationality) {
        this.id = id;
        this.name = coachName;
        this.nationality = nationality;
    }

    public Coach(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public Coach(){};

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String coachName) {
        this.name = coachName;
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
        return Objects.equals(id, coach.id) && Objects.equals(name, coach.name) && Objects.equals(nationality, coach.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nationality);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", coachName='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
