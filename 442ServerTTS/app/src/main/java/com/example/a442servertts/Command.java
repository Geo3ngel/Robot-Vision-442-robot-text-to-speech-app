package com.example.a442servertts;

import java.util.ArrayList;

public class Command {

    private String result;
    private ArrayList<String> requiredPhrases;

    public Command(String result, ArrayList<String> requiredPhrases){
        this.result = result;
        this.requiredPhrases = requiredPhrases;
    }

    // Checks that all needed phrases are present in the command
    public boolean checkForPhrases(String[] command){
        for (String x : requiredPhrases){
            System.out.println("Checking Phrase: "+x);
            if(!contains(command, x)){
                return false;
            }
        }
        return true;
    }

    // Returns true if the required Phrases includes the String s
    private boolean contains(String[] s, String check){
        for (String x : s){
            if(x.equalsIgnoreCase(check)){
                return true;
            }
        }
        return false;
    }

    public String getResult(){
        return result;
    }
}
