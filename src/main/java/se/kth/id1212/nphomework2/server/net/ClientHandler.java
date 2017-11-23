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
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
/*import se.kth.id1212.nio.textprotocolchat.common.Constants;
import se.kth.id1212.nio.textprotocolchat.common.MessageException;
import se.kth.id1212.nio.textprotocolchat.common.MessageSplitter;
import se.kth.id1212.nio.textprotocolchat.common.MsgType;*/

/**
 * Handles all communication with one particular chat client.
 */
class ClientHandler implements Runnable {
    private static final String JOIN_MESSAGE = " joined conversation.";
    private static final String LEAVE_MESSAGE = " left conversation.";
    private static final String USERNAME_DELIMETER = ": ";

    private final ClientConnection server;
    private final SocketChannel clientChannel;
    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(8192);
    //private final MessageSplitter msgSplitter = new MessageSplitter();
    private String username = "anonymous";
    private SelectionKey clientKey;

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
    }
    
    public void setKey(SelectionKey key){
        this.clientKey = key;
    }

    /**
     * Receives and handles one message from the connected client.
     */
    @Override
    public void run() {
        /*while (msgSplitter.hasNext()) {
            Message msg = new Message(msgSplitter.nextMsg());
            switch (msg.msgType) {
                case USER:
                    username = msg.msgBody;
                    server.broadcast(username + JOIN_MESSAGE);
                    break;
                case ENTRY:
                    server.broadcast(username + USERNAME_DELIMETER + msg.msgBody);
                    break;
                case DISCONNECT:
                    server.broadcast(username + LEAVE_MESSAGE);
                    break;
                default:
                    throw new MessageException("Received corrupt message: " + msg.receivedString);
            }
        }*/
        int i = 0;
        if(i<1){
            String message = "hello";
            /*ByteBuffer buff = createBroadcastMessage(message);
            try {
                sendMsg(buff);
            } catch (IOException ex) {
                
            }*/
            server.broadcast(message,clientKey);
            i++;
        }
    }
    
    private ByteBuffer createBroadcastMessage(String msg) {
        /*StringJoiner joiner = new StringJoiner(Constants.MSG_TYPE_DELIMETER);
        joiner.add(MsgType.BROADCAST.toString());
        joiner.add(msg);
        String messageWithLengthHeader = MessageSplitter.prependLengthHeader(joiner.toString());*/
        return ByteBuffer.wrap(msg.getBytes());
    }

    /**
     * Sends the specified message to the connected client.
     *
     * @param msg The message to send.
     * @throws IOException If failed to send message.
     */
    void sendMsg(ByteBuffer msg) throws IOException {
      
        clientChannel.write(msg);
        if (msg.hasRemaining()) {
            //throw new MessageException("Could not send message");
        }
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
        String recvdString = extractMessageFromBuffer();
        //msgSplitter.appendRecvdString(recvdString);
        //System.out.println(recvdString);
        ForkJoinPool.commonPool().execute(this);
        System.out.println(recvdString);
        /*String message = "hello";
            ByteBuffer buff = createBroadcastMessage(message);
            try {
                sendMsg(buff);
            } catch (IOException ex) {
                
       }*/
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

    private static class Message {
        //private MsgType msgType;
        private String msgBody;
        private String receivedString;

        private Message(String receivedString) {
            //parse(receivedString);
            this.receivedString = receivedString;
        }
/*
        private void parse(String strToParse) {
            try {
                String[] msgTokens = strToParse.split(Constants.MSG_TYPE_DELIMETER);
                msgType = MsgType.valueOf(msgTokens[Constants.MSG_TYPE_INDEX].toUpperCase());
                if (hasBody(msgTokens)) {
                    msgBody = msgTokens[Constants.MSG_BODY_INDEX].trim();
                }
            } catch (Throwable throwable) {
                throw new MessageException(throwable);
            }
        }
*/
        private boolean hasBody(String[] msgTokens) {
            return msgTokens.length > 1;
        }
    }
}
