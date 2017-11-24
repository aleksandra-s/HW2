/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.server.controller;

import se.kth.id1212.nphomework2.common.Commands;
import se.kth.id1212.nphomework2.common.Commands.Command;
import se.kth.id1212.nphomework2.server.model.Player;

/**
 *
 * @author aleks_uuia3ly
 */
//CLASS DESCRIPTION: Handles command sent in from client to server and communicates with Player class to update game

public class ServerController {
    private String command = "";
    private String rest = "";
    private Player player = new Player();
    private boolean guessedCorrect = false;
    
    //HANDLES COMMAND SENT TO SERVER FROM CLIENT AND RETURNS AN INFORMATIVE STRING
    public String handleCommand(String clientCommand){
        String inputFromClient = clientCommand;
        int i = inputFromClient.indexOf(' ');
                Command cmd;
                
                if(i < 0){
                    if(inputFromClient.equals("disconnect")){
                        cmd = Command.DISCONNECT;
                    }
                    else if(inputFromClient.equals("start")){
                        cmd = Command.START_GAME;
                    }
                    else if(inputFromClient.equals("info")){
                        cmd = Command.GET_INFO;
                    }
                    else if(inputFromClient.equals("connect")){
                        cmd = Command.CONNECT;
                    }
                    else{
                        cmd = Command.UNKNOWN;
                    }
                }
                else{
                    command = inputFromClient.substring(0,i);
                    rest = inputFromClient.substring(i + 1);
                    Commands commandClass = new Commands();
                    cmd = commandClass.getEnumFromString(command,rest);
                }
                
                switch (cmd) {
                    case CONNECT:
                        return("Connected");
                    case DISCONNECT:
                        return("Disconnected");
                    case START_GAME:
                        player.getNewWord();
                        break;
                    case GUESS_LETTER:
                        if(!player.checkStillPlaying()){
                            return("Not playing");
                        }
                        guessedCorrect = player.guessLetter(rest.charAt(0));
                        break;
                    case GUESS_WORD:
                        if(!player.checkStillPlaying()){
                            return("Not playing");
                        }
                        guessedCorrect = player.guessWord(rest);
                        break;
                    case UNKNOWN:
                        return("Invalid command sent to server");
                }
                if(player.checkStillPlaying()){ //Based on state of current game, return corresponding info to be sent to client
                            return("playing," + player.getWordToPrint() + "," + player.getAttemptsLeft());
                        }
                        else{
                            if(guessedCorrect){
                                return("won," + player.getWordToPrint() + "," + player.getScore());
                            }
                            else{
                                return("lost," + player.getGuessWord() + "," + player.getScore());
                            }
                        }
                }
    }
    