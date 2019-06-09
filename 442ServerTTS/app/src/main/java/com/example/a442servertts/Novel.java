package com.example.a442servertts;

import java.util.ArrayList;

public class Novel {

    private String title;
    private String author;
    private ArrayList<Character> characters;
    private ArrayList<String> themes;
    private ArrayList<String> motifs;
    private ArrayList<String> symbols;
    private ArrayList<String> keyfacts;
    private ArrayList<String> quotesOfNote;

    public Novel(String Title, String Author){
        title = Title;
        author = Author;
        characters = new ArrayList<>();
        themes = new ArrayList<String>();
        motifs = new ArrayList<String>();
        symbols= new ArrayList<String>();
        keyfacts= new ArrayList<String>();
        quotesOfNote= new ArrayList<String>();
    }

    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public Character getCharacter(String s){
        for (Character x : characters){
            if(x.getName().equalsIgnoreCase(s)){
                return x;
            }
        }
        Character c = new Character("Missing", "You fool");
        return c;
    }
    public String getTheme(int i){
        return themes.get(i);
    }
    public String getMotif(int i ){
        return motifs.get(i);
    }
    public String getSymbol(int i){
        return symbols.get(i);
    }
    public String getKeyFact(int i){
        return keyfacts.get(i);
    }public String getQuote(int i){
        return quotesOfNote.get(i);
    }


    public void addCharacter(String name, String description){
        Character c = new Character(name, description);
        characters.add(c);
    }

    // Typical Setters
    public void addTheme(String s){
        themes.add(s);
    }
    public void addMotif(String s){
        motifs.add(s);
    }
    public void addSymbol(String s){
        symbols.add(s);
    }
    public void addQuote(String s){
        quotesOfNote.add(s);
    }
    public void addKeyfact(String s){
        keyfacts.add(s);
    }
}
