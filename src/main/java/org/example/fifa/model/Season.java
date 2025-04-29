package org.example.fifa.model;

import org.example.fifa.model.enums.Status;

import java.util.Objects;

public class Season {
    private String id;
    private Status status;
    private int year;
    private String alias;

    public Season(String id, Status status, int year, String alias) {
        this.id = id;
        this.status = status;
        this.year = year;
        this.alias = alias;
    }

    public Season() {}

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return year == season.year && Objects.equals(id, season.id) && status == season.status && Objects.equals(alias, season.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, year, alias);
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", status=" + status +
                ", year=" + year +
                ", alias='" + alias + '\'' +
                '}';
    }
}


