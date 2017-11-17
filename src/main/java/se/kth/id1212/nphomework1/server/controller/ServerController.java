/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework1.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import se.kth.id1212.nphomework1.common.Commands;
import se.kth.id1212.nphomework1.common.Commands.Command;
import se.kth.id1212.nphomework1.server.model.Player;

//***ServerController methods main, serve, and startHandler, and class ClientHandler based on Leif Lindback's code***

/**
 *
 * @author aleks_uuia3ly
 */
public class ServerController {
     private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private int portNo = 8080;

    /**
     * @param args Takes one command line argument, the number of the port on which the server will
     *             listen, the default is <code>8080</code>.
     */
    public static void main(String[] args) {
        ServerController server = new ServerController();
        server.serve();
    }
    
    private void serve() {
        try {
            ServerSocket listeningSocket = new ServerSocket(portNo);
            //System.out.println("In serve");
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
    }
    
    private void startHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler handler = new ClientHandler(this, clientSocket);
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY); //NOT SURE ABOUT THIS
        handlerThread.start();
    }
    
    public class ClientHandler implements Runnable {
    private final ServerController server;
    private final Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private Player player;
    private boolean connected;
    private boolean guessedCorrect;
    String command;
    String rest;

    /**
     * Creates a new instance, which will handle communication with one specific client connected to
     * the specified socket.
     *
     * @param clientSocket The socket to which this handler's client is connected.
     */
    ClientHandler(ServerController server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        connected = true;
        this.player = new Player();
        this.guessedCorrect = false;
    }

    /**
     * The run loop handling all communication with the connected client.
     */
    @Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
            System.out.println("Client socket accepted");
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        if(connected){
            System.out.println("connected");
        }
        while (connected) {
            try {
                String inputFromClient = fromClient.readLine();
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
                        toClient.println("Connected");
                        break;
                    case DISCONNECT:
                        disconnectClient();
                        toClient.println("Disconnected");
                        break;
                    case START_GAME:
                        player.getNewWord();
                        break;
                    case GUESS_LETTER:
                        if(!player.checkStillPlaying()){
                            toClient.println("Not playing");
                            break;
                        }
                        guessedCorrect = player.guessLetter(rest.charAt(0));
                        break;
                    case GUESS_WORD:
                        if(!player.checkStillPlaying()){
                            toClient.println("Not playing");
                            break;
                        }
                        guessedCorrect = player.guessWord(rest);
                        break;
                    case GET_INFO:
                        if(player.checkStillPlaying()){ //still playing
                            toClient.println("playing," + player.getWordToPrint() + "," + player.getAttemptsLeft());
                        }
                        else{
                            if(guessedCorrect){
                                toClient.println("won," + player.getWordToPrint() + "," + player.getScore());
                            }
                            else{
                                toClient.println("lost," + player.getGuessWord() + "," + player.getScore());
                            }
                        }
                        break;
                    case UNKNOWN:
                        toClient.println("Invalid command sent to server");
                        break;
                }
                
            } catch (IOException ioe) {
                disconnectClient();
            }
        }
    }
    
    private void disconnectClient() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
    }
  }
}



