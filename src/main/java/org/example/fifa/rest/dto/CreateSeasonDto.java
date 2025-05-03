package org.example.fifa.rest.dto;

public class CreateSeasonDto {
    private int year;
    private String alias;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
