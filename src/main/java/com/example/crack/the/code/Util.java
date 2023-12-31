/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author danie
 */
public class Util {

    private static final String WORD_TO_DIGIT_MAPPINGS_FILE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "word-to-digit.json";
    private static Map<String, String> wordToDigitMap = readMappingsFromJsonFile();

    private static Logger logger;

    

    Util(Logger logger) {
        this.logger = logger;
        
    }
    
    // returns a list with the index and the digit
    // every entry in the list has both the index and the digit
    public static Map<Integer, ArrayList<Integer>> getDigits(String input) {
        // read the word-to-digit.json file

        if (input == null || input.length() == 0) {
            logger.severe("Error: input is null or empty");
            
            // print to logger who called this metho (the method and line that called this method)
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stackTraceElement = stackTraceElements[2];
            logger.severe("Error: " + stackTraceElement.getClassName() + " " + stackTraceElement.getMethodName() + " " + stackTraceElement.getLineNumber());



        }


        // key = digit, value = indexs
        Map<Integer, ArrayList<Integer>> digitToIndexMap = new HashMap<>();

        // check if any digits are in the input
        char[] inputChars = input.toCharArray();
        for(char c: inputChars){
            if(Character.isDigit(c)){
                // if the digit is already in the map, add the index to the arraylist
                if(digitToIndexMap.containsKey(Character.getNumericValue(c))){
                    digitToIndexMap.get(Character.getNumericValue(c)).add(input.indexOf(c));
                } else {
                    // if the digit is not in the map, create a new arraylist and add the index to it
                    ArrayList<Integer> indexs = new ArrayList<>();
                    indexs.add(input.indexOf(c));
                    digitToIndexMap.put(Character.getNumericValue(c), indexs);
                }
            }
        }

        // if any digits are found and there is digits as words as well 
        if (digitToIndexMap.size() > 0 && hasDigitsAsWords(input)) {
            //logger.warn("Warning: there are digits as words and digits as numbers in the input");
            logger.warning("Warning: there are digits as words and digits as numbers in the input");
            
        }
        
        if (hasDigitsAsWords(input)) {
            //logger.info("Info: there are digits as words in the input");
            logger.info("Info: there are digits as words in the input");
            input = convertNumbersToDigits(input);

            // update the digitToIndexMap
            digitToIndexMap = new HashMap<>();
            inputChars = input.toCharArray();
            for(char c: inputChars){
                if(Character.isDigit(c)){
                    // if the digit is already in the map, add the index to the arraylist
                    if(digitToIndexMap.containsKey(Character.getNumericValue(c))){
                        digitToIndexMap.get(Character.getNumericValue(c)).add(input.indexOf(c));
                    } else {
                        // if the digit is not in the map, create a new arraylist and add the index to it
                        ArrayList<Integer> indexs = new ArrayList<>();
                        indexs.add(input.indexOf(c));
                        digitToIndexMap.put(Character.getNumericValue(c), indexs);
                    }
                }
            }
            
            if (digitToIndexMap.size() == 0) {
                logger.severe("Error: there are no digits in the input and returned true for hasDigitsAsWords");
            }




        }



       
        // key = digit, value = indexs
        




        

        return digitToIndexMap;
    }
    

    // true if it has digits written as words 
    public static boolean hasDigitsAsWords(String input) {
        // read the word-to-digit.json file
        String[] words = input.split(" ");

        // Iterate over the words and check if any of them are digits
        for (String word : words) {
            if (wordToDigitMap.containsKey(word)) {
                return true;
            }
        }

        return false;
    }


    public static String convertNumbersToDigits(String input) {
        // read the word-to-digit.json file
        String[] words = input.split(" ");

        StringBuilder sb = new StringBuilder();

        // Iterate over the words and append the corresponding digit to the StringBuilder
        for (String word : words) {
            if (wordToDigitMap.containsKey(word)) {
                sb.append(wordToDigitMap.get(word) + " ");
            } else {
                sb.append(word + " ");
            }

        }


        // now we need to make sure that if 2 digits are next to each other, we remove the space between them   
        // (so that we don't get 20 5 instead of 25)
        ArrayList<String> wordsList = new ArrayList<String>(Arrays.asList(sb.toString().split(" ")));

        for (int i = 0; i < wordsList.size(); i++) {
            try{
                Integer num = Integer.parseInt(wordsList.get(i));

                if(num != null){
                        Integer nextNum = Integer.parseInt(wordsList.get(i+1));
                        if(nextNum != null){
                            Integer sum = num + nextNum;
                            
                            // the first 2 word number between 0-1000 will be 20, so if num 1 is less than 20, print a warning
                            if(num < 20){
                                System.out.println("Warning: the first number in the combination is less than 20, so the first 2 word numbers will be added together");
                                //logger.warn("Warning: the first number in the combination is less than 20, so the first 2 word numbers will be added together");
                                logger.warning("Warning: the first number in the combination is less than 20, so the first 2 word numbers will be added together");
                            }
                    
                    
                            wordsList.remove(i);
                            wordsList.remove(i);
                            wordsList.add(i, sum.toString());
                        }
                    }
                } catch (NumberFormatException e) {
                    // not a number
                    // do nothing
                }
            }

        // convert the arraylist back to a string
        sb = new StringBuilder();

        for (String word : wordsList) {
            sb.append(word + " ");
        }

        return sb.toString();
        


       
    }

    private static Map<String, String> readMappingsFromJsonFile() {
        // Read word-to-digit mappings from the JSON file
        Map<String, String> wordToDigitMap = null;
        try {
            wordToDigitMap = new ObjectMapper().readValue(new File(WORD_TO_DIGIT_MAPPINGS_FILE_PATH), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordToDigitMap;
    }

    
}
