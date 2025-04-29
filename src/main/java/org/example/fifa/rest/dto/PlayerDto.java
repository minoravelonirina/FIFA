package org.example.fifa.rest.dto;

import org.example.fifa.model.Club;
import org.example.fifa.model.enums.Position;

public class PlayerDto {
    private String id;
    private Club club;
    private Position position;
    private String nationality;
    private int age;
    private String name;
    private int number;

    public PlayerDto(String id, Club club, Position position, String nationality, int age, String name, int number) {
        this.id = id;
        this.club = club;
        this.position = position;
        this.nationality = nationality;
        this.age = age;
        this.name = name;
        this.number = number;
    }

    public PlayerDto(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
