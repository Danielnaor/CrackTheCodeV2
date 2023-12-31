/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.crack.the.code;

import java.io.File;
import java.lang.reflect.Array;

/**
 *
 * @author danie
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class createClueObjectFromText {

    Boolean all = false;
    Boolean nothing = false;




    /*
     * This class will take in a string and create a clue object from it.
     * 
     * the challlenge is that the string might be in a few formats. 
     * examples of formats and what should be created from them:
     
     test case 1:
     given string: 9, 2, 8, 5 one number is correct but wrong placed
     expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(9, 2, 8, 5))
                    .hintMessage("one number is correct but wrong placed")
                    .correctDigits(1)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(1)
                    .build();

        test case 2:
        given string: 0 1 2 3 One number is correct and well placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 1, 2, 3))
                    .hintMessage("One number is correct and well placed")
                    .correctDigits(1)
                    .wellPlacedDigits(1)
                    .incorrectlyPlacedDigits(0)
                    .build();

        test case 3:
        given string: 1 3 2 0 one number is correct but wrongly placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(1, 3, 2, 0))
                    .hintMessage("one number is correct but wrongly placed")
                    .correctDigits(1)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(1)
                    .build();

        test case 4:
        given string: 0 0 0 0 nothing is correct
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 0, 0, 0))
                    .hintMessage("nothing is correct")
                    .correctDigits(0)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(0)
                    .build();

        test case 5:
        given string: 0 6 7 2 numbers are correct but wrong placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 6, 7))
                    .hintMessage("2 numbers are correct but wrong placed")
                    .correctDigits(2)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(2)
                    .build();

        in order to deal with ones like number 5, we would check the string part and if there is no either of the following words:
        "all"
        "nothing"
        any number (words - one, two, three, four, five, six, seven, eight, nine)
        then we would use the last numebr of combination as the number of correct digits (regardless of whether it is well placed or not)
        */


        public void createClueObjectFromText(String clueString) {
            
            // if first character of clueString is a space, remove it
        if (clueString.charAt(0) == ' ') {
            clueString = clueString.substring(1);
        }

        // if we are writing numbers as words, we can try to assume that the digits would all be part of the combination.
        // i think if 3 digits are present written as words, then we can assume that the digits (numbers as digits) would be part of the combination

        



     // figure out what is the separator for the combination
        // the separator is all the characters from the first character that is not a number to the character before the next number
        boolean foundFirstNum = false;
        String combinationSeparator = "";
        for (int i = 0; i < clueString.length(); i++) {
            if (!Character.isDigit(clueString.charAt(i))) {
                combinationSeparator += clueString.charAt(i);
                System.out.println("combinationSeparatorscan: " + combinationSeparator);
            } else if (foundFirstNum) {
                break;
            } else {
                foundFirstNum = true;
            }
        }

        

        

        System.out.print("combinationSeparator: " + combinationSeparator); 
        // if space is the separator, then print (space)
        if (combinationSeparator.equals(" ")) {
            System.out.println("(space)");
        } else {
            System.out.println();
        }

        Scanner scanner = new Scanner(clueString);
        scanner.useDelimiter(combinationSeparator);
        List<Integer> combination = new ArrayList<>();
        while (scanner.hasNextInt()) {
            combination.add(scanner.nextInt());

        }

        // first index of the last number of the combination
        Integer lastNum = combination.get(combination.size() - 1);
        int indexLastNumCombo = clueString.indexOf(lastNum.toString());

        

        String hintMessage = clueString.substring(indexLastNumCombo + lastNum.toString().length() + 1);
 
        // if first character of hint message is a space, remove it
        if (hintMessage.charAt(0) == ' ') {
            hintMessage = hintMessage.substring(1);
        }

        System.out.println("hintMessage: " + hintMessage);


        // now if the first character of the hint message is a not a letter or a number try reseting and using the seperator of the combination as the seperator for the hint message
        if (!Character.isLetter(hintMessage.charAt(0)) && !Character.isDigit(hintMessage.charAt(0))) {
            

            scanner = new Scanner(clueString);
            scanner.useDelimiter(combinationSeparator);
            combination = new ArrayList<>();
            while (scanner.hasNextInt()) {
                combination.add(scanner.nextInt());
            }

            // first index of the last number of the combination
            lastNum = combination.get(combination.size() - 1);
            indexLastNumCombo = clueString.indexOf(lastNum.toString());

            

            hintMessage = clueString.substring(indexLastNumCombo + lastNum.toString().length() + 1);
    
            // if first character of hint message is a space, remove it
            if (hintMessage.charAt(0) == ' ') {
                hintMessage = hintMessage.substring(1);
            }

            // now if the first character of the hint message is a not a letter or a number print an error message
            if (!Character.isLetter(hintMessage.charAt(0)) && !Character.isDigit(hintMessage.charAt(0))) {
                throw new IllegalArgumentException("ERROR: first character of hint message is not a letter or a number");
            }
        }


        


        

        // now we will need to check if the hint message has either:
        // "all"
        // "nothing"
        // any number (words - one, two, three, four, five, six, seven, eight, nine)
        // any number (digits - 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        // if it does, then we will use the last number of the combination as the number of correct digits (regardless of whether it is well placed or not)

        
        checkIfAllOrNothing(hintMessage);

        // if both all and nothing are false, then we will check if any number (words - one, two, three, four, five, six, seven, eight, nine) appears in the hint message
        if (!all && !nothing) {

            
            System.out.println("hintMessage: " + hintMessage);
            // convert all numbers as words to numbers as digits using the Util class.
            hintMessage = Util.convertNumbersToDigits(hintMessage);


            //  how many times a number any number (words - one, two, three, four, five, six, seven, eight, nine) appears in the hint message
            int numDigits = 0;

            ArrayList<Integer> digitsInHintMessage = new ArrayList<Integer>();

            // split the string into words - to avoid getting numbers that are 2 digits long (like 25) and counting them as 2 digits
            String[] words = clueString.split(" ");
            for (String word : words) {
                try {
                    Integer num = Integer.parseInt(word);
                    if (num != null) {
                        numDigits++;
                        digitsInHintMessage.add(num);
                    }
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }

            System.out.println("numDigits: " + numDigits);
            System.out.println("digitsInHintMessage: " + digitsInHintMessage);

        
        }














    }


        private void checkIfAllOrNothing(String clueString) {
        if (clueString.contains("all")) {
            all = true;

        } else if (clueString.contains("nothing")) {
            nothing = true;
        } 

    }

    // testing 
    public static void main(String[] args) {
        createClueObjectFromText createClueObjectFromText = new createClueObjectFromText();
        createClueObjectFromText.createClueObjectFromText("9, 2, 8, 5 one number is correct but wrong placed");
        createClueObjectFromText.createClueObjectFromText("0 1 2 3 One number is correct and well placed");
         
    }

    
}
