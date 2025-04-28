package org.example.fifa.model;

import org.example.fifa.model.enums.Position;

import java.util.Objects;

public class Player {
    private Long id;
    private String playerName;
    private int number;
    private int age;
    private Position position;
    private String nationality;

    public Player(Long id, String playerName, int number, int age, Position position, String nationality) {
        this.id = id;
        this.playerName = playerName;
        this.number = number;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
    }

    public Player(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return number == player.number && age == player.age && Objects.equals(id, player.id) && Objects.equals(playerName, player.playerName) && position == player.position && Objects.equals(nationality, player.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerName, number, age, position, nationality);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", number=" + number +
                ", age=" + age +
                ", position=" + position +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
