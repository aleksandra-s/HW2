/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.client.view;

/**
 *
 * @author aleks_uuia3ly
 */
//CLASS DESCRIPTION: Methods for printing to user

public class OutputHandler{

    public void printWord(String word){
        System.out.println(word);
    }
    
    public void printAttempts(String attempts){
        System.out.println("Failed attempts left: " + attempts);
    }
    
    public void printScore(String score){
        System.out.println("Score: " + score);
    }
    
    public void printLoss(){
        System.out.println("You lost :(");
    }
    
    public void printWin(){
        System.out.println("You won :)");
    }
    
    public void printLn(String toPrint){
        System.out.println(toPrint);
    }
    
    public void print(String toPrint){
        System.out.print(toPrint);
    }
}
