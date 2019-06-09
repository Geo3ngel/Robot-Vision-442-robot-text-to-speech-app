package com.example.a442servertts;

public class Character {

    private String name;
    private String desc;

    public Character(String name, String description){
        this.name = name;
        this.desc = description;
    }

    public String getName(){
        return name;
    }
    public String getDesc(){
        return desc;
    }
}
