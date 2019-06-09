package com.example.a442servertts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProcessCommand {

    public ProcessCommand(){
        initCommands();

    }

    private ArrayList<Command> commands;

    public void initCommands(){
        commands = new ArrayList<>();

        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("Grab");keywords.add("Marker");
        Command c = new Command("Yes Master", keywords);
        commands.add(c);

        keywords = new ArrayList<String>();
        keywords.add("This");keywords.add("is");keywords.add("a");keywords.add("test");
        c = new Command("Damn right its a test.", keywords);
        commands.add(c);
    }

    private Random rand = new Random();
    // Takes in string, breaks it down into words, puts them in a list, and checks if they match any command listed.
    public String process(String command){

        String[] wrds = command.trim().split("\\s+");
        // Splits up command string into individual words for processing
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(wrds));

        // If words contains all of the necessary phrases for a command, return/run that command.
        for( Command com : commands){
            if(com.checkForPhrases(wrds)){
                return com.getResult();
            }
        }

        return "Sorry, could you repeat that?";
    }
}
