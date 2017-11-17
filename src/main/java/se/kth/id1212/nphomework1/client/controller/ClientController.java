/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework1.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import se.kth.id1212.nphomework1.client.view.OutputHandler;

/**
 *
 * @author aleks_uuia3ly
 */
public class ClientController {
    
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    //private PrintWriter toServer;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private volatile boolean connected;
    private OutputHandler toOutput;
    
    public void connect(String ipAddress, int port)throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ipAddress, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener()).start();
        toOutput = new OutputHandler();
        toServer.println("connect");  
    }
    
    public void startGame(){
        if(connected){
            toServer.println("start");
            toServer.println("info");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    public void guessLetter(String letter){
        if(connected){
            toServer.println("guess " + letter);
            toServer.println("info");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    public void guessWord(String word){
        if(connected){
            toServer.println("guess " + word);
            toServer.println("info");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    public void disconnect() throws IOException{
        if(connected){
            toServer.println("disconnect");
            socket.close();
            socket = null;
            connected = false;
            toOutput.printLn("Disconnected");
            toOutput.print("> ");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }
    }
    
    private void sendToPrint(String input){
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
                //toOutput.print("Guess again");
            }
        }
    }
    
    public boolean checkConnected(){
        return connected;
    }
    
    private class Listener implements Runnable {

        @Override
        public void run() {
            try {
                for (;;) {
                    String input = fromServer.readLine();
                    sendToPrint(input);
                    //System.out.println(input);
                    
                }
            } catch (IOException connectionFailure) {
                if (connected) {
                    sendToPrint("Lost connection.");
                }
            }
        }
    
    }
}
