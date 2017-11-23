/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.server.net;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 *
 * @author aleks_uuia3ly
 */
public class MsgAndKeyObject {
    private final SelectionKey clientKey;
    private final ByteBuffer msgToSend;
    
    MsgAndKeyObject(ByteBuffer msg, SelectionKey key){
        this.msgToSend = msg;
        this.clientKey = key;
    }
    
    public SelectionKey getKey(){
        return this.clientKey;
    }
    
    public ByteBuffer getMessage(){
        return this.msgToSend;
    }
}
