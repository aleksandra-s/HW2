/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;
import se.kth.id1212.nphomework2.server.controller.ServerController;
 /* Handles all communication with one particular chat client.
 */
class ClientHandler implements Runnable {

    private final ClientConnection server;
    private final SocketChannel clientChannel;
    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(8192);
    private String clientCommand = "";
    private SelectionKey clientKey;
    private ServerController contr;

    /**
     * Creates a new instance, which will handle communication with one specific client connected to
     * the specified channel.
     *
     * @param clientChannel The socket to which this handler's client is connected.
     */
    ClientHandler(ClientConnection server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
        //this.clientKey = key;
        contr = new ServerController();
    }
    
    public void setKey(SelectionKey key){
        this.clientKey = key;
    }

    /**
     * Receives and handles one message from the connected client.
     */
    @Override
    public void run() {
        String returnString = contr.handleCommand(clientCommand); 
        if(returnString != null){
            if(returnString.equals("Disconnected")){
                try {
                    disconnectClient();
                } catch (IOException ex) {
                    server.broadcast("Couldn't disconnect", clientKey);
                }
            }
            else{
                server.broadcast(returnString, clientKey); //(1)
            }
        }
    }

    /**
     * Sends the specified message to the connected client.
     *
     * @param msg The message to send.
     * @throws IOException If failed to send message.
     */
    void sendMsg(ByteBuffer msg) throws IOException {
        clientChannel.write(msg);
    }

    /**
     * Reads a message from the connected client, then submits a task to the default
     * <code>ForkJoinPool</code>. That task which will handle the received message.
     *
     * @throws IOException If failed to read message
     */
    void recvMsg() throws IOException {
        msgFromClient.clear();
        int numOfReadBytes;
        numOfReadBytes = clientChannel.read(msgFromClient);
        if (numOfReadBytes == -1) {
            throw new IOException("Client has closed connection.");
        }
        clientCommand = extractMessageFromBuffer();
        ForkJoinPool.commonPool().execute(this);
        System.out.println(clientCommand);
    }

    private String extractMessageFromBuffer() {
        msgFromClient.flip();
        byte[] bytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(bytes);
        return new String(bytes);
    }

    /**
     * Closes this instance's client connection.
     *
     * @throws IOException If failed to close connection.
     */
    void disconnectClient() throws IOException {
        clientChannel.close();
    }
}
