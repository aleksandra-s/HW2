/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import se.kth.id1212.nphomework2.client.view.OutputHandler;
import se.kth.id1212.nphomework2.client.net.ServerConnection;

/**
 *
 * @author aleks_uuia3ly
 */
public class ClientController {
    
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private volatile boolean connected;
    private OutputHandler toOutput;
    private ServerConnection serverConnect;
    //Connect to server
    public void connect(String ipAddress, int port)throws IOException {
        /*socket = new Socket();
        socket.connect(new InetSocketAddress(ipAddress, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener()).start();
        toOutput = new OutputHandler();
        toServer.println("connect");  */
        serverConnect = new ServerConnection();
        serverConnect.connect(ipAddress, port);
    }
    
    //Send start game command to server and ask for info to print
    public void startGame(){
        /*if(connected){
            toServer.println("start");
            toServer.println("info");
        }
        else{
            toOutput.printLn("Not connected, type connect <address> <port> to connect");
            toOutput.print("> ");
        }*/
        serverConnect.sendMsg("start a 127");
    }
    
     //Send guess letter command to server and ask for info to print
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
    
     //Send guess word command to server and ask for info to print
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
    
     //Send disconnect command to server and shut down connection on client side
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
    
    //Send information to OutputHandler to be printed to user
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
            }
        }
    }
    
    public boolean checkConnected(){
        return connected;
    }
    
    //Listener subclass waits on message from server
    private class Listener implements Runnable {

        @Override
        public void run() {
            try {
                for (;;) {
                    String input = fromServer.readLine();
                    sendToPrint(input);
                    
                }
            } catch (IOException connectionFailure) {
                if (connected) {
                    sendToPrint("Lost connection.");
                }
            }
        }
    
    }
}
