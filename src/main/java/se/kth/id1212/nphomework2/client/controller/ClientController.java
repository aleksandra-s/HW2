/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.client.controller;

import java.io.IOException;
import se.kth.id1212.nphomework2.client.view.OutputHandler;
import se.kth.id1212.nphomework2.client.net.ServerConnection;

/**
 *
 * @author aleks_uuia3ly
 */

/*CLASS DESCRIPTION: Handles inputs from InputHandler (client View), communicates with ServerConnection, and handles printing out to OutputHandler
*/
public class ClientController {
    private volatile boolean connected;
    private OutputHandler toOutput = new OutputHandler();
    private ServerConnection serverConnect;
    
    //CONNECT TO SERVER
    public void connect(String ipAddress, int port)throws IOException {
        serverConnect = new ServerConnection(this);
        serverConnect.connect(ipAddress, port);
        connected = true;
    }
    
    //SEND START GAME COMMAND TO SERVER 
    public void startGame(){
        if(connected){
            serverConnect.sendMsg("start");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    
     //SEND GUESS LETTER COMMAND TO SERVER
    public void guessLetter(String letter){
        if(connected){
            serverConnect.sendMsg("guess " + letter);
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
     //SEND GUESS WORD COMMAND TO SERVER
    public void guessWord(String word){
        if(connected){
            serverConnect.sendMsg("guess " + word);
            //serverConnect.sendMsg("info");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
     //SEND DISCONNECT COMMAND TO SERVE AND DISCONNECT
    public void disconnect() throws IOException{
        if(connected){
            serverConnect.sendMsg("disconnect");
            serverConnect.disconnect();
            connected = false;
            toOutput.printLn("Disconnected");
            toOutput.print("> ");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    //SEND INFO TO OutputHandler CLASS TO BE PRINTED TO THE USER
    public void sendToPrint(String input){
        int i = input.indexOf(',');
        if(i < 0){
            toOutput.printLn(input);
            toOutput.print("> ");
        }
        else{
            String determine = input.substring(0,i);
            String rest = input.substring(i+1);
            if(determine.equals("won")){
                toOutput.printWin();
                int j = rest.indexOf(',');
                String guessWord = rest.substring(0,j);
                String score = rest.substring(j+1);
                toOutput.printWord(guessWord);
                toOutput.printScore(score);
                toOutput.printLn("Type start to play again");
                toOutput.print("> ");
            }
            else if(determine.equals("lost")){
                toOutput.printLoss();
                int j = rest.indexOf(',');
                String guessWord = rest.substring(0,j);
                String score = rest.substring(j+1);
                toOutput.printWord(guessWord);
                toOutput.printScore(score);
                toOutput.printLn("Type start to play again");
                toOutput.print("> ");
            }
            else if(determine.equals("playing")){
                int j = rest.indexOf(',');
                String guessWord = rest.substring(0,j);
                String attempts = rest.substring(j+1);
                toOutput.printWord(guessWord);
                toOutput.printAttempts(attempts);
                toOutput.print("> ");
            }
        }
    }
    
    public boolean checkConnected(){
        return connected;
    }
}
