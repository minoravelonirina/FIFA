package org.example.fifa.rest.dto;

import java.util.Objects;

public class PlayerScorerDto {
    private String id;
    private String name;
    private int number;

    public PlayerScorerDto(String id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public PlayerScorerDto(){};

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerScorerDto that = (PlayerScorerDto) o;
        return number == that.number && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number);
    }

    @Override
    public String toString() {
        return "PlayerScorerDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
