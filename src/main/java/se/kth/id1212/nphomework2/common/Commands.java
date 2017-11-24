/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.common;

/**
 *
 * @author aleks_uuia3ly
 */
//CLASS DESCRIPTION: Contains common enum Command and parsing method 

public class Commands {
    public enum Command{
        CONNECT,
        DISCONNECT,
        START_GAME,
        GUESS_LETTER,
        GUESS_WORD,
        UNKNOWN,
        GET_INFO
    }
    
      public Command getEnumFromString(String command, String rest){ //This is almost the same as method in InputHandler, but method in InputHandler could be switched if UI changes
        Command returnCommand;
        if(command.equals("disconnect")){
            returnCommand = Command.DISCONNECT;
        }
        else if(command.equals("start")){
            returnCommand = Command.START_GAME;
        }
        else if(command.equals("guess")){
            //System.out.println(rest);
            if(rest.length() > 1){
                returnCommand = Command.GUESS_WORD;
            }
            else{
                returnCommand = Command.GUESS_LETTER;
            }
        }
        else if(command.equals("info")){
            returnCommand = Command.GET_INFO;
        }
        else{
            returnCommand = Command.UNKNOWN;
        }
        return returnCommand;
    }
    
}
