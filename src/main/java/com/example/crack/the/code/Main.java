/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

/**
 *
 * @author danie
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String MAPPINGS_FILE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "word-to-digit.json";

    public static void main(String[] args) throws IOException {
        // Read word-to-digit mappings from the JSON file
        Map<String, String> wordToDigitMap = readMappingsFromJsonFile();

        // Your clueString containing numbers as words
        // test case 1: 25
        String clueString = "twenty five is the number of letters in this sentence";

        // Split the clueString into words
        String[] words = clueString.split(" ");

        // store the new text with the words replaced with numbers
        StringBuilder sb = new StringBuilder();

        // Iterate over the words and append the corresponding digit to the StringBuilder
        for (String word : words) {
            if (wordToDigitMap.containsKey(word)) {
                sb.append(wordToDigitMap.get(word) + " ");
            } else {
                sb.append(word + " ");
            }

        }


        System.out.println(sb.toString());

        // in order not to get:
        // 20 5 is the number of letters in this sentence
        // and instead get
        // 25 is the number of letters in this sentence
        // (instead of 20 5 we want 25)
        // we will need to check if any of the digits are 2 digits only a space away from each other
        // if they are, then we will need to remove the space between them and add the 2 digits together

        ArrayList<String> wordsSliced = new ArrayList<String>(Arrays.asList(sb.toString().split(" ")));

        for (int i = 0; i < wordsSliced.size() - 1; i++) {
            try{

                // check if term at index i is a number (can be longer than 1 digit)
                    Integer num1 = Integer.parseInt(wordsSliced.get(i));
                    // check if num1 is actually a number
                    if (num1 != null){
                        Integer num2 = Integer.parseInt(wordsSliced.get(i + 1));
                        System.out.println("num1: " + num1);
                        System.out.println("num2: " + num2);

                        // check if num2 is actually a number
                        if (num2 != null){
                            // since we know that they are both right after each other, we can just add them together
                            Integer sum = num1 + num2;
                            System.out.println("sum: " + sum);

                            // remove the 2 numbers from the array
                            wordsSliced.remove(i);
                            wordsSliced.remove(i);

                            // add the sum to the array
                            wordsSliced.add(i, sum.toString());
                        }

                    }
            } catch (NumberFormatException e){
                    // do nothing
            }
                       
        }

        // convert the arraylist back to a string
        sb = new StringBuilder();
        for (String word : wordsSliced) {
            sb.append(word + " ");
        }

        System.out.println(sb.toString());
        




    }
 
    // can replace this by just using the objectmapper
    
    private static Map<String, String> readMappingsFromJsonFile() throws IOException {
        // Create an ObjectMapper to handle JSON serialization/deserialization
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and map it to a Java Map
        File mappingsFile = new File(MAPPINGS_FILE_PATH);
        return objectMapper.readValue(mappingsFile, Map.class);
    }

}