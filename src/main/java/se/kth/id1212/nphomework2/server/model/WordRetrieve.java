/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.nphomework2.server.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aleks_uuia3ly
 */
public class WordRetrieve {
    public String chooseWordFromFile() throws IOException {
        // Take the words from the word.txt file
        try (FileInputStream getFile = new FileInputStream("/Users/aleks_uuia3ly/Documents/NetBeansProjects/NPHomework1/src/main/resources/words.txt");
             BufferedReader getData = new BufferedReader(new InputStreamReader(new DataInputStream(getFile)))) {

            String word = "";
            //Get a random word from the txt file
            int random = (int) Math.round(Math.random() * 43020);
            for (int i = 0; i < random; i++) {
                word = getData.readLine();
            }
            return word;
        } catch (IOException ex) {
            throw ex;
        }
    }
}
