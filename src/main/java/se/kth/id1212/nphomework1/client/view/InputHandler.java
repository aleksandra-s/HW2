/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework1.client.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import se.kth.id1212.nphomework1.client.controller.ClientController;
import se.kth.id1212.nphomework1.common.Commands.Command;
/**
 *
 * @author aleks_uuia3ly
 */
public class InputHandler implements Runnable{

    private boolean receivingCmds = false;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String command;
    String rest;
    ClientController clientController;
    
    public void start(){
        if(receivingCmds){
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
        clientController = new ClientController();
    }
    
    @Override
    public void run(){  
        System.out.println("Welcome to Hangman. Type 'connect <address> <port>' to connect, then 'start' to start playing");
        System.out.print("> ");
        while(receivingCmds){
            try{
                String clientInput = reader.readLine();
                int i = clientInput.indexOf(' ');
                Command cmd;
                if(i < 0){
                    if(clientInput.equals("disconnect")){
                        cmd = Command.DISCONNECT;
                    }
                    else if(clientInput.equals("start")){
                        cmd = Command.START_GAME;
                    }
                    else{
                        cmd = Command.UNKNOWN;
                    }
                }
                else{
                    command = clientInput.substring(0,i);
                    rest = clientInput.substring(i + 1);
                    cmd = getCommand(command,rest);
                }
                switch(cmd){
                    case CONNECT:
                        
                        int j = rest.indexOf(' ');
                        String address = rest.substring(0,j);
                        String port = rest.substring(j + 1);
                        clientController.connect(address,Integer.parseInt(port));
                        System.out.println("Connecting " + address + " " + port);
                        break;
                    case DISCONNECT:
                        System.out.println("Sending disconnect command");
                        clientController.disconnect();
                        break;
                    case START_GAME:
                        if(clientController.checkConnected()){
                            System.out.println("Starting game- type 'guess' followed by a letter or word");
                            clientController.startGame();
                        }
                        else{
                            System.out.println("Not connected");
                            System.out.print("> ");
                        }
                        break;
                    case GUESS_LETTER:
                        if(clientController.checkConnected()){
                            System.out.println("Guessing letter " + rest);
                            clientController.guessLetter(rest);
                        }
                        else{
                            System.out.println("Not connected");
                            System.out.print("> ");
                        }
                        break;
                    case GUESS_WORD:
                        if(clientController.checkConnected()){
                            System.out.println("Guessing word");
                            clientController.guessWord(rest);
                        }
                        else{
                            System.out.println("Not connected");
                            System.out.print("> ");
                        }
                        break;
                    case UNKNOWN:
                        System.out.println("Invalid command");
                        System.out.print("> ");
                        break;
                }
            }catch(Exception e){
                System.out.println("Operation failed");
                e.printStackTrace();
                System.out.print("> ");
            }
        }
    }
    
    private Command getCommand(String command, String rest){
        Command returnCommand;
        if(command.equals("connect")){
            returnCommand = Command.CONNECT;
        }
        else if(command.equals("guess")){
            if(rest.length() > 1){
                returnCommand = Command.GUESS_WORD;
            }
            else{
                returnCommand = Command.GUESS_LETTER;
            }
        }
        else{
            returnCommand = Command.UNKNOWN;
        }
        return returnCommand;
    }
}
