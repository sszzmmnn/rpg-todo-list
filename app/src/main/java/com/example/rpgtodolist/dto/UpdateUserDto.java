package com.example.rpgtodolist.dto;

public class UpdateUserDto {
    String name;
    UserStatsDto value;
    String hash;

    public UpdateUserDto(String name, UserStatsDto value, String hash) {
        this.name = name;
        this.value = value;
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatsDto getValue() {
        return value;
    }

    public void setValue(UserStatsDto value) {
        this.value = value;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ",\"value\":" + value +
                ",\"hash\":\"" + hash + '\"' +
                '}';
    }
}
