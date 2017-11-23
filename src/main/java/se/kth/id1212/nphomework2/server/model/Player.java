/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.server.model;

import java.io.IOException;
/*import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id1212.nphomework1.server.ServerController;
import se.kth.id1212.nphomework1.server.ServerController.ClientHandler;*/

/**
 *
 * @author aleks_uuia3ly
 */
public class Player {
    private int score;
    private int attemptsLeft;
    private String wordToGuess;
    private String wordToPrint;
    private boolean stillPlaying;
    
    public Player(){
        this.score = 0;
        this.attemptsLeft = 0;
        this.wordToGuess = "";
        this.wordToPrint = "";
        this.stillPlaying = false;
    }
    
    //Start new game
    public String getNewWord(){
        String returnWord;
        try {
            returnWord = new se.kth.id1212.nphomework1.server.model.WordRetrieve().chooseWordFromFile();
            System.out.println("returned: " + returnWord);
            this.wordToGuess = returnWord;
            StringBuilder buildMask = new StringBuilder();
            for(int i = 0; i < returnWord.length(); i++){
                buildMask.append("_ ");
            }
            this.wordToPrint = buildMask.toString();
            this.attemptsLeft = returnWord.length();
            this.stillPlaying = true;
            return wordToGuess;
        } catch (IOException ex) {
            System.out.println("Unable to get word");
            ex.printStackTrace();
            return null;
        }
        
    }
    
    public int getAttemptsLeft(){
        return this.attemptsLeft;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public String getWordToPrint(){
        return this.wordToPrint;
    }
    
    public boolean checkStillPlaying(){
        return this.stillPlaying;
    }
    
    //Update client game data based on whether guess is correct
    public boolean guessWord(String guess){
        if(guess.equals(this.wordToGuess)){
            this.wordToPrint = this.wordToGuess;
            this.score++;
            this.stillPlaying = false;
            return true;
        }
        else{
            if(this.attemptsLeft >= 1){
                attemptsLeft--;
                if(this.attemptsLeft == 0){
                    this.stillPlaying = false;
                    this.score--;
                }
            }
            return false;
        }
    }
    
    //Update client game data based on whether guess is correct
    public boolean guessLetter(char guess){
        boolean returnResult = false;
        for(int i = 0; i < this.wordToGuess.length(); i++){
            if(this.wordToGuess.charAt(i) == guess){
                returnResult = true;
                updateWordToPrint(i,guess);
            }
        }
        if(this.wordToPrint.equals(this.wordToGuess)){
            this.stillPlaying = false;
            this.score++;
        }
        if(!returnResult){
            if(this.attemptsLeft >= 1){
                attemptsLeft--;
                if(this.attemptsLeft == 0){
                    this.stillPlaying = false;
                    this.score--;
                }
            }
        }
        return returnResult;
    }
    
    public String getGuessWord(){
        return this.wordToGuess;
    }
    
    private void updateWordToPrint(int index, char replacement){
        StringBuilder updatePrint = new StringBuilder(this.wordToPrint);
        for(int i = 0; i < updatePrint.length(); i++){
            if(updatePrint.charAt(i) == ' '){
                updatePrint.deleteCharAt(i);
            }
        }
        updatePrint.setCharAt(index,replacement);
        for(int i = 0; i < updatePrint.length(); i++){
            if(updatePrint.charAt(i) == '_'){
                updatePrint.insert(i + 1, ' ');
            }
        }
        this.wordToPrint = updatePrint.toString();
    }
}
